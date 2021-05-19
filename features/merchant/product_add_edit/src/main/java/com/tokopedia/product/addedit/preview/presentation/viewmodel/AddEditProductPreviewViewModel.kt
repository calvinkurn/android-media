package com.tokopedia.product.addedit.preview.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.constant.ProductStatus
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_PHOTOS
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.draft.domain.usecase.GetProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper.mapDraftToProductInputModel
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.domain.mapper.GetProductMapper
import com.tokopedia.product.addedit.preview.domain.usecase.GetProductUseCase
import com.tokopedia.product.addedit.preview.domain.usecase.GetShopInfoLocationUseCase
import com.tokopedia.product.addedit.preview.domain.usecase.ValidateProductNameUseCase
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.DRAFT_SHOWCASE_ID
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.productlimitation.domain.model.ProductLimitationData
import com.tokopedia.product.addedit.productlimitation.domain.usecase.ProductLimitationUseCase
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.domain.usecase.AnnotationCategoryUseCase
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ValidationResultModel
import com.tokopedia.product.addedit.variant.presentation.model.ValidationResultModel.Result.*
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.shop.common.graphql.data.shopopen.SaveShipmentLocation
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ShopOpenRevampSaveShipmentLocationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductPreviewViewModel @Inject constructor(
        private val getProductUseCase: GetProductUseCase,
        private val getProductMapper: GetProductMapper,
        private val resourceProvider: ResourceProvider,
        private val getProductDraftUseCase: GetProductDraftUseCase,
        private val saveProductDraftUseCase: SaveProductDraftUseCase,
        private val validateProductNameUseCase: ValidateProductNameUseCase,
        private val getShopInfoLocationUseCase: GetShopInfoLocationUseCase,
        private val saveShopShipmentLocationUseCase: ShopOpenRevampSaveShipmentLocationUseCase,
        private val authorizeAccessUseCase: AuthorizeAccessUseCase,
        private val authorizeEditStockUseCase: AuthorizeAccessUseCase,
        private val userSession: UserSessionInterface,
        private val annotationCategoryUseCase: AnnotationCategoryUseCase,
        private val productLimitationUseCase: ProductLimitationUseCase,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val productId = MutableLiveData<String>()
    private val detailInputModel = MutableLiveData<DetailInputModel>()
    private var draftId = ""
    var productDomain: Product = Product()

    // observing the product id, and will become true if product id exist
    val isEditing = Transformations.map(productId) { id ->
        (!id.isNullOrBlank() || productInputModel.value?.productId.orZero() != 0L) && !isDuplicate
    }
    val isAdding: Boolean get() = getProductId().isBlank()
    var isDuplicate: Boolean = false

    private val mIsProductManageAuthorized = MutableLiveData<Result<Boolean>>()
    val isProductManageAuthorized: LiveData<Result<Boolean>>
        get() = mIsProductManageAuthorized

    // observing the product id, and will execute the use case or check authorization when product id is changed
    // also, observing whether user role is authorized and will execute use case if true
    private val mGetProductResult = MediatorLiveData<Result<Product>>().apply {
        addSource(productId) {
            if (!productId.value.isNullOrBlank())  {
                getProductData(it)
            } else {
                // Authorize access if adding product
                authorizeAccess()
            }
        }
        addSource(mIsProductManageAuthorized) { result ->
            mIsLoading.value = false
            ((result as? Success)?.data)?.let { shouldLoadProductData ->
                productId.value?.let {
                    if (shouldLoadProductData && it.isNotBlank()) {
                        getProductData(it)
                    }
                }
            }
        }
    }
    val getProductResult: LiveData<Result<Product>> get() = mGetProductResult

    // observing the use case result, and will convert product data to input model
    var productInputModel = MediatorLiveData<ProductInputModel>()

    // observing the use case result, and will become true if no variant
    val isVariantEmpty = Transformations.map(mGetProductResult) {
        when (it) {
            is Success -> {
                it.data.variant.products.isEmpty()
            }
            is Fail -> {
                true
            }
        }
    }

    private val mImageUrlOrPathList = MutableLiveData<MutableList<String>>()
    val imageUrlOrPathList: LiveData<MutableList<String>> get() = mImageUrlOrPathList

    private val mGetProductDraftResult = MutableLiveData<Result<ProductDraft>>()
    val getProductDraftResult: LiveData<Result<ProductDraft>> get() = mGetProductDraftResult

    private val mIsLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = mIsLoading

    private val mValidationResult = MutableLiveData<ValidationResultModel>()
    val validationResult: LiveData<ValidationResultModel> get() = mValidationResult

    private val mLocationValidation = MutableLiveData<Result<Boolean>>()
    val locationValidation: LiveData<Result<Boolean>> get() = mLocationValidation

    private val mSaveShopShipmentLocationResponse = MutableLiveData<Result<SaveShipmentLocation>>()
    val saveShopShipmentLocationResponse: LiveData<Result<SaveShipmentLocation>> get() = mSaveShopShipmentLocationResponse

    private val saveProductDraftResultMutableLiveData = MutableLiveData<Result<Long>>()
    val saveProductDraftResultLiveData: LiveData<Result<Long>> get() = saveProductDraftResultMutableLiveData

    private val mProductLimitationData = MutableLiveData<Result<ProductLimitationData>>()
    val productLimitationData: LiveData<Result<ProductLimitationData>> get() = mProductLimitationData

    // Enable showing ticker if seller has multi location shop
    val shouldShowMultiLocationTicker
        get() = isAdding && userSession.isMultiLocationShop && (userSession.isShopOwner || userSession.isShopAdmin)

    init {
        with (productInputModel) {
            addSource(mGetProductResult) {
                productInputModel.value = when (it) {
                    is Success -> {
                        if (productInputModel.value?.isDataChanged == true) {
                            return@addSource
                        }
                        productDomain = it.data
                        val productInputModel = getProductMapper.mapRemoteModelToUiModel(it.data)

                        // duplicate product handling
                        if (!isDuplicate) {
                            productInputModel.productId = it.data.productID.toLongOrZero()
                        } else {
                            productInputModel.itemSold = 0 // reset item sold when duplicate product
                            productInputModel.detailInputModel.currentProductName = ""
                            updateSpecificationFromRemote(productInputModel.detailInputModel.categoryId, it.data.productID)
                        }

                        // decrement wholesale min order by one because of > symbol
                        val initialWholeSaleList =  productInputModel.detailInputModel.wholesaleList
                        val actualWholeSaleList = decrementWholeSaleMinOrder(initialWholeSaleList)

                        // reassign wholesale information with the actual wholesale values
                        productInputModel.detailInputModel.wholesaleList = actualWholeSaleList

                        // filter the show cases from auto generated showcase from BE (showcase with draftId for desktop product with no showcase information)
                        val showCases = productInputModel.detailInputModel.productShowCases
                        val filteredShowCases = showCases.filter { showcaseItemPicker -> showcaseItemPicker.showcaseId != DRAFT_SHOWCASE_ID }

                        // reassign product show cases information
                        productInputModel.detailInputModel.productShowCases = filteredShowCases

                        productInputModel
                    }
                    is Fail -> {
                        if (productInputModel.value?.isDataChanged == true) {
                            return@addSource
                        }
                        ProductInputModel()
                    }
                }
            }
            addSource(detailInputModel) {
                productInputModel.value?.let { productInputModel ->
                    productInputModel.detailInputModel = it
                    this@AddEditProductPreviewViewModel.productInputModel.value = productInputModel
                }
            }
            addSource(getProductDraftResult) {
                productInputModel.value = when(it) {
                    is Success -> {
                        if (it.data.productId == 0L) getProductLimitation() // obtain data when draft mode entered
                        val productInputModel = mapDraftToProductInputModel(it.data)
                        productInputModel
                    }
                    is Fail -> {
                        AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                        ProductInputModel()
                    }
                }
            }
        }
    }

    fun getProductId(): String {
        return productId.value ?: ""
    }

    fun getDraftId(): Long {
        return if (draftId.isBlank()) 0 else draftId.toLong()
    }

    fun setProductId(id: String) {
        productId.value = id
    }

    fun setDraftId(id: String) {
        draftId = id
    }

    fun setIsDuplicate(isDuplicate: Boolean) {
        this.isDuplicate = isDuplicate
    }

    fun updateProductPhotos(imagePickerResult: ArrayList<String>, originalImageUrl: ArrayList<String>, editted: ArrayList<Boolean>) {
        productInputModel.value?.let {
            val pictureList = it.detailInputModel.pictureList.filter { pictureInputModel ->
                originalImageUrl.contains(pictureInputModel.urlOriginal)
            }.filterIndexed { index, _ -> !editted[index] }

            val imageUrlOrPathList = imagePickerResult.mapIndexed { index, urlOrPath ->
                if (!editted[index]) {
                    val picture = pictureList.find { pict -> pict.urlOriginal == originalImageUrl[index] }?.urlThumbnail.toString()
                    if(picture != "null" && picture.isNotBlank()) {
                        return@mapIndexed picture
                    }
                }
                urlOrPath
            }.toMutableList()

            this.detailInputModel.value = it.detailInputModel.apply {
                this.pictureList = pictureList
                this.imageUrlOrPathList = imageUrlOrPathList
            }

            this.mImageUrlOrPathList.value = imageUrlOrPathList
        }
    }

    fun updateProductStatus(isActive: Boolean) {
        productInputModel.value?.let {
            val newStatus = if (isActive) ProductStatus.STATUS_ACTIVE else ProductStatus.STATUS_INACTIVE
            it.detailInputModel.status = newStatus
            it.variantInputModel.products.forEach { variant ->
                variant.status = if (isActive) ProductStatus.STATUS_ACTIVE_STRING else ProductStatus.STATUS_INACTIVE_STRING
            }
        }
    }

    fun getNewProductInputModel(imageUrlOrPathList: ArrayList<String>): ProductInputModel {
        val detailInputModel = DetailInputModel().apply { this.imageUrlOrPathList = imageUrlOrPathList }
        return ProductInputModel().apply { this.detailInputModel = detailInputModel }
    }

    fun getProductData(productId: String) {
        mIsLoading.value = true
        val isAuthorized = (mIsProductManageAuthorized.value as? Success)?.data ?: false
        if (isAuthorized) {
            launchCatchError(block = {
                val data = withContext(Dispatchers.IO) {
                    getProductUseCase.params = GetProductUseCase.createRequestParams(productId)
                    getProductUseCase.executeOnBackground()
                }
                mGetProductResult.value = Success(data)
                mIsLoading.value = false
            }, onError = {
                mGetProductResult.value = Fail(it)
            })
        } else {
            authorizeAccess()
        }
    }

    fun getProductDraft(draftId: Long) {
        launchCatchError(block = {
            getProductDraftUseCase.params = GetProductDraftUseCase.createRequestParams(draftId)
            mGetProductDraftResult.value = withContext(Dispatchers.IO) {
                getProductDraftUseCase.executeOnBackground()
            }.let { Success(it) }
        }, onError = {
            mGetProductDraftResult.value = Fail(it)
        })
    }

    fun saveProductDraft(productDraft: ProductDraft, productId: Long, isUploading: Boolean) {
        launchCatchError(block = {
            saveProductDraftUseCase.params = SaveProductDraftUseCase.createRequestParams(productDraft, productId, isUploading)
            saveProductDraftResultMutableLiveData.value = withContext(Dispatchers.IO) {
                saveProductDraftUseCase.executeOnBackground()
            }.let { Success(it) }
        }, onError = {
            saveProductDraftResultMutableLiveData.value = Fail(it)
        })
    }

    fun getProductLimitation() {
        launchCatchError(block = {
            val result = withContext(dispatcher.io) {
                productLimitationUseCase.executeOnBackground()
            }
            mProductLimitationData.value = Success(result.productAddRule.data)
        }, onError = {
            mProductLimitationData.value = Fail(it)
        })
    }

    fun validateProductInput(detailInputModel: DetailInputModel): String {
        var errorMessage = ""
        // validate category input
        if (detailInputModel.categoryId.isEmpty() || detailInputModel.categoryId == "0")  {
            errorMessage = resourceProvider.getInvalidCategoryIdErrorMessage() ?: ""
        }

        // validate images empty
        if (detailInputModel.imageUrlOrPathList.isEmpty())  {
            errorMessage = resourceProvider.getInvalidPhotoCountErrorMessage() ?: ""
        }

        // validate images already reached limit
        if (detailInputModel.imageUrlOrPathList.size > MAX_PRODUCT_PHOTOS)  {
            errorMessage = resourceProvider.getInvalidPhotoReachErrorMessage() ?: ""
        }

        return errorMessage
    }

    fun incrementWholeSaleMinOrder(wholesaleList: List<WholeSaleInputModel>) : List<WholeSaleInputModel> {
        wholesaleList.forEach { wholesaleInputModel ->
            // recalculate wholesale min order because of > symbol
            val oldValue = wholesaleInputModel.quantity.toBigInteger()
            val newValue = oldValue + 1.toBigInteger()
            wholesaleInputModel.quantity = newValue.toString()
        }
        return wholesaleList
    }

    fun decrementWholeSaleMinOrder(wholesaleList: List<WholeSaleInputModel>) : List<WholeSaleInputModel> {
        wholesaleList.forEach { wholesaleInputModel ->
            // recalculate wholesale min order because of > symbol
            val oldValue = wholesaleInputModel.quantity.toBigInteger()
            val newValue = oldValue - 1.toBigInteger()
            wholesaleInputModel.quantity = newValue.toString()
        }
        return wholesaleList
    }

    fun setIsDataChanged(isChanged: Boolean) {
        productInputModel.value?.isDataChanged = isChanged
    }

    fun getIsDataChanged(): Boolean {
        productInputModel.value?.let {
            return it.isDataChanged
        }
        return false
    }

    fun validateProductNameInput(productName: String) {
        mIsLoading.value = true
        productInputModel.value?.let {
            it.detailInputModel.apply {
                if (productName == currentProductName) {
                    mValidationResult.value = ValidationResultModel(VALIDATION_SUCCESS)
                    mIsLoading.value = false
                    return
                }
            }
        }
        launchCatchError(block = {
            val response = withContext(dispatcher.io) {
                validateProductNameUseCase.setParamsProductName(productId.value, productName)
                validateProductNameUseCase.executeOnBackground()
            }
            val validationMessages = response.productValidateV3.data.validationResults
            val validationResult = if (validationMessages.isEmpty())
                VALIDATION_SUCCESS else VALIDATION_ERROR
            val validationException = MessageErrorException(validationMessages.joinToString("\n"))

            mValidationResult.value = ValidationResultModel(validationResult, validationException)
            mIsLoading.value = false
        }, onError = {
            mValidationResult.value = ValidationResultModel(VALIDATION_ERROR, it)
            mIsLoading.value = false
        })
    }

    fun validateShopLocation(shopId: Int) {
        mIsLoading.value = true
        launchCatchError(block = {
            getShopInfoLocationUseCase.params = GetShopInfoLocationUseCase.createRequestParams(shopId)
            val shopLocation = withContext(Dispatchers.IO) {
                getShopInfoLocationUseCase.executeOnBackground()
            }
            mLocationValidation.value = Success(shopLocation)
            mIsLoading.value = false
        }, onError = {
            mLocationValidation.value = Fail(it)
            mIsLoading.value = false
        })
    }

    fun saveShippingLocation(dataParam: MutableMap<String, Any>) {
        mIsLoading.value = true
        launchCatchError(block = {
            withContext(dispatcher.io) {
                saveShopShipmentLocationUseCase.params = ShopOpenRevampSaveShipmentLocationUseCase.createRequestParams(dataParam)
                val saveShipmentLocationData = saveShopShipmentLocationUseCase.executeOnBackground()
                saveShipmentLocationData.let {
                    mSaveShopShipmentLocationResponse.postValue(Success(it))
                }
            }
            mIsLoading.value = false
        }) {
            mSaveShopShipmentLocationResponse.value = Fail(it)
            mIsLoading.value = false
        }
    }

    fun resetValidateResult() {
        mValidationResult.value?.result = UNVALIDATED
        mValidationResult.value?.exception = Exception()
    }

    fun updateSpecificationFromRemote(categoryId: String, productId: String) {
        mIsLoading.value = true
        launchCatchError(block = {
            val result = withContext(dispatcher.io) {
                annotationCategoryUseCase.setParamsCategoryId(categoryId)
                annotationCategoryUseCase.setParamsProductId(productId)
                val response = annotationCategoryUseCase.executeOnBackground()
                response.drogonAnnotationCategoryV2.data
            }
            updateSpecificationByAnnotationCategory(result)
            mIsLoading.value = false
        }, onError = {
            AddEditProductErrorHandler.logExceptionToCrashlytics(it)
            mIsLoading.value = false
        })
    }

    fun updateSpecificationByAnnotationCategory(annotationCategoryList: List<AnnotationCategoryData>) {
        val result: MutableList<SpecificationInputModel> = mutableListOf()
        annotationCategoryList.forEach {
            val selectedValue = it.data.firstOrNull { value -> value.selected }
            selectedValue?.apply {
                val specificationInputModel = SpecificationInputModel(id.toString(), name)
                result.add(specificationInputModel)
            }
        }

        productInputModel.value?.apply {
            detailInputModel.specifications = result
        }
    }

    private fun authorizeAccess() {
        mIsLoading.value = true
        launchCatchError(
                block = {
                    mIsProductManageAuthorized.value = Success(withContext(dispatcher.io) {
                        if (userSession.isShopOwner) {
                            true
                        } else {
                            val accessId =
                                    when {
                                        isAdding -> AccessId.PRODUCT_ADD
                                        isDuplicate -> AccessId.PRODUCT_DUPLICATE
                                        isEditing.value == true -> AccessId.PRODUCT_EDIT
                                        else -> AccessId.PRODUCT_ADD
                                    }
                            userSession.shopId.toLongOrZero().let { shopId ->
                                val canManageProduct = async {
                                    val requestParams = AuthorizeAccessUseCase.createRequestParams(shopId, accessId)
                                    authorizeAccessUseCase.execute(requestParams)
                                }
                                val canEditStock = async {
                                    val requestParams = AuthorizeAccessUseCase.createRequestParams(shopId, AccessId.EDIT_STOCK)
                                    authorizeEditStockUseCase.execute(requestParams)
                                }
                                canManageProduct.await() && canEditStock.await()
                            }
                        }
                    })
                },
                onError = {
                    mIsProductManageAuthorized.value = Fail(it)
                }
        )
    }

}