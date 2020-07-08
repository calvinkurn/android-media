package com.tokopedia.product.addedit.preview.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.addedit.common.constant.ProductStatus
import com.tokopedia.product.addedit.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.domain.usecase.GetProductVariantUseCase
import com.tokopedia.product.addedit.description.presentation.model.PictureViewModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantCombinationViewModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantOptionParent
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_PHOTOS
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.draft.domain.usecase.GetProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper.mapDraftToProductInputModel
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.domain.GetProductUseCase
import com.tokopedia.product.addedit.preview.domain.mapper.GetProductMapper
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.TYPE_ACTIVE
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.TYPE_ACTIVE_LIMITED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.TYPE_WAREHOUSE
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductPreviewViewModel @Inject constructor(
        private val getProductUseCase: GetProductUseCase,
        private val getProductMapper: GetProductMapper,
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val resourceProvider: ResourceProvider,
        private val getProductDraftUseCase: GetProductDraftUseCase,
        private val saveProductDraftUseCase: SaveProductDraftUseCase,
        dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val productId = MutableLiveData<String>()
    private val detailInputModel = MutableLiveData<DetailInputModel>()

    // observing the product id, and will become true if product id exist
    val isEditing = Transformations.map(productId) { id ->
        (!id.isNullOrBlank() || productInputModel.value?.productId.orZero() != 0L) && !isDuplicate
    }

    val productAddResult = MutableLiveData<ProductInputModel>()

    // observing the product id, and will execute the use case when product id is changed
    private val mGetProductResult = MediatorLiveData<Result<Product>>().apply {
        addSource(productId) {
            if (!productId.value.isNullOrBlank()) getProductData(it)
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

    private val mProductVariantList = MutableLiveData<Result<List<ProductVariantByCatModel>>>()
    val productVariantList: LiveData<Result<List<ProductVariantByCatModel>>> get() = mProductVariantList
    val productVariantListData: List<ProductVariantByCatModel>? get() = mProductVariantList.value.let {
        when(it) {
            is Success -> it.data
            else -> null
        }
    }

    private val mGetProductDraftResult = MutableLiveData<Result<ProductDraft>>()
    val getProductDraftResult: LiveData<Result<ProductDraft>> get() = mGetProductDraftResult

    private val mIsLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = mIsLoading

    val isAdding =  MediatorLiveData<Boolean>().apply {
        addSource(productId) {
            it.isNullOrBlank()
        }
        addSource(productInputModel) {
            it.didBackPress
        }
    }

    var isDuplicate: Boolean = false

    private var draftId = ""

    var productDomain: Product = Product()

    var hasOriginalVariantLevel: Boolean = false // indicating whether you can clear variant or not

    val hasWholesale: Boolean
        get() = productInputModel.value?.detailInputModel?.wholesaleList?.isNotEmpty() ?: false

    private val saveProductDraftResultMutableLiveData = MutableLiveData<Result<Long>>()
    val saveProductDraftResultLiveData: LiveData<Result<Long>> get() = saveProductDraftResultMutableLiveData

    init {
        with (productInputModel) {
            addSource(mGetProductResult) {
                productInputModel.value = when (it) {
                    is Success -> {
                        productDomain = it.data
                        val productInputModel = getProductMapper.mapRemoteModelToUiModel(it.data)
                        if (!isDuplicate) {
                            productInputModel.productId = it.data.productID.toLongOrZero()
                        }

                        // decrement wholesale min order by one because of > symbol
                        val initialWholeSaleList =  productInputModel.detailInputModel.wholesaleList
                        val actualWholeSaleList = decrementWholeSaleMinOrder(initialWholeSaleList)

                        // reassign wholesale information with the actual wholesale values
                        productInputModel.detailInputModel.wholesaleList = actualWholeSaleList

                        getVariantList(productInputModel.detailInputModel.categoryId)
                        hasOriginalVariantLevel = checkOriginalVariantLevel(productInputModel)
                        productInputModel
                    }
                    is Fail -> ProductInputModel()
                }
            }
            addSource(detailInputModel) {
                getVariantList(it.categoryId)
                productInputModel.value?.let { productInputModel ->
                    productInputModel.detailInputModel = it
                    this@AddEditProductPreviewViewModel.productInputModel.value = productInputModel
                }
            }
            addSource(getProductDraftResult) {
                productInputModel.value = when(it) {
                    is Success -> {
                        val productInputModel = mapDraftToProductInputModel(it.data)
                        getVariantList(productInputModel.detailInputModel.categoryId)
                        hasOriginalVariantLevel = checkOriginalVariantLevel(productInputModel)
                        productInputModel
                    }
                    is Fail -> {
                        AddEditProductErrorHandler.logExceptionToCrashlytics(it.throwable)
                        ProductInputModel()
                    }
                }
            }
            addSource(productAddResult) {
                productInputModel.value = it
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
        val pictureList = productInputModel.value?.detailInputModel?.pictureList?.filter {
            originalImageUrl.contains(it.urlOriginal)
        }?.filterIndexed { index, _ -> !editted[index] }.orEmpty()

        val imageUrlOrPathList = imagePickerResult.mapIndexed { index, urlOrPath ->
            if (editted[index]) urlOrPath else pictureList.find { it.urlOriginal == originalImageUrl[index] }?.urlThumbnail ?: urlOrPath
        }.toMutableList()

        this.detailInputModel.value = productInputModel.value?.detailInputModel?.apply {
            this.pictureList = pictureList
            this.imageUrlOrPathList = imageUrlOrPathList
        } ?: DetailInputModel(pictureList = pictureList, imageUrlOrPathList = imageUrlOrPathList)

        this.mImageUrlOrPathList.value = imageUrlOrPathList
    }

    fun updateVariantAndOption(productVariant: ArrayList<ProductVariantCombinationViewModel>,
                               variantOptionParent: ArrayList<ProductVariantOptionParent>) {
        productInputModel.value?.variantInputModel?.productVariant =
                mapProductVariant(productVariant, variantOptionParent)
        productInputModel.value?.variantInputModel?.variantOptionParent =
                mapVariantOption(variantOptionParent)
    }

    fun updateSizeChart(productSizeChart: PictureViewModel?) {
        productInputModel.value?.variantInputModel?.productSizeChart = productSizeChart
    }

    fun updateProductStatus(isActive: Boolean) {
        val newStatus = if (isActive) ProductStatus.STATUS_ACTIVE else ProductStatus.STATUS_INACTIVE
        productInputModel.value?.detailInputModel?.status = newStatus
        productInputModel.value?.variantInputModel?.productVariant?.forEach {
            it.st = newStatus
        }
    }

    fun getNewProductInputModel(imageUrlOrPathList: ArrayList<String>): ProductInputModel {
        val detailInputModel = DetailInputModel().apply { this.imageUrlOrPathList = imageUrlOrPathList }
        return ProductInputModel().apply { this.detailInputModel = detailInputModel }
    }

    fun getProductData(productId: String) {
        mIsLoading.value = true
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
    }

    fun getVariantList(categoryId: String) {
        mIsLoading.value = true
        launchCatchError(block = {
            mProductVariantList.value = Success(withContext(Dispatchers.IO) {
                getProductVariantUseCase.params =
                        GetProductVariantUseCase.createRequestParams(categoryId)
                getProductVariantUseCase.executeOnBackground()
            })
            mIsLoading.value = false
        }, onError = {
            mProductVariantList.value = Fail(it)
        })
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

    private fun mapProductVariant(productVariant: ArrayList<ProductVariantCombinationViewModel>,
                                  variantOptionParent: ArrayList<ProductVariantOptionParent>
    ): ArrayList<ProductVariantCombinationViewModel> {
        productVariant.forEach { variant ->
            val options: ArrayList<Int> = ArrayList()
            val level1Id = getVariantOptionIndex(variant.level1String, variantOptionParent)
            val level2Id = getVariantOptionIndex(variant.level2String, variantOptionParent)
            level1Id?.let { options.add(it) }
            level2Id?.let { options.add(it) }
            variant.opt = options
        }
        return productVariant
    }

    private fun mapVariantOption(variantOptionParent: ArrayList<ProductVariantOptionParent>):
            ArrayList<ProductVariantOptionParent> = variantOptionParent.map {
        it.productVariantOptionChild?.forEachIndexed { index, productVariantOptionChild ->
            productVariantOptionChild.pvo = index + 1
        }
        it
    } as ArrayList<ProductVariantOptionParent>

    private fun getVariantOptionIndex(variantValue: String?,
                                      variantOptionParent: List<ProductVariantOptionParent>): Int? {
        variantOptionParent.forEach { productVariantOptionParent ->
            productVariantOptionParent.productVariantOptionChild?.let {
                it.forEachIndexed { outputIndex, optionChild ->
                    if (optionChild.value == variantValue) return outputIndex + 1
                }
            }
        }
        return null
    }

    fun getStatusStockViewVariant(): Int {
        val isActive: Boolean = productInputModel.value?.detailInputModel?.status == 1
        val stockCount: Int = productInputModel.value?.detailInputModel?.stock ?: 0
        return if (!isActive) {
            TYPE_WAREHOUSE
        } else if (isActive && stockCount > 0) {
            TYPE_ACTIVE_LIMITED
        } else {
            TYPE_ACTIVE
        }
    }

    // disable removing variant when in edit mode and if product have a variant
    fun checkOriginalVariantLevel(inputModel: ProductInputModel): Boolean {
        val variantInputModel  = inputModel.variantInputModel
        variantInputModel.apply {
            if (isEditing.value == true) {
                if (productVariant.size > 0) {
                    return variantOptionParent.getOrNull(0) != null
                }
            }
        }
        return false
    }

}