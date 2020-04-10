package com.tokopedia.product.addedit.preview.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.domain.usecase.GetProductVariantUseCase
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.draft.domain.usecase.GetProductDraftUseCase
import com.tokopedia.product.addedit.mapper.mapDraftToProductInputModel
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.domain.GetProductUseCase
import com.tokopedia.product.addedit.preview.domain.mapper.GetProductMapper
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductPreviewViewModel @Inject constructor(
        private val getProductUseCase: GetProductUseCase,
        private val getProductMapper: GetProductMapper,
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val resourceProvider: ResourceProvider,
        private val getProductDraftUseCase: GetProductDraftUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val productId = MutableLiveData<String>()

    private val draftId = MutableLiveData<String>()

    private val detailInputModel = MutableLiveData<DetailInputModel>()

    // observing the product id, and will become true if product id exist
    val isEditing = Transformations.map(productId) { id ->
        !id.isNullOrBlank()
    }

    val isDrafting = Transformations.map(draftId) { id ->
        !id.isNullOrBlank()
    }

    // observing the product id, and will execute the use case when product id is changed
    private val mGetProductResult = MediatorLiveData<Result<Product>>().apply {
        addSource(productId) {
            if (!productId.value.isNullOrBlank()) loadProductData(it)
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

    private val mGetProductDraftResult = MutableLiveData<Result<ProductDraft>>()
    val getProductDraftResult: LiveData<Result<ProductDraft>> get() = mGetProductDraftResult

    init {
        with (productInputModel) {
            addSource(mGetProductResult) {
                productInputModel.value = when (it) {
                    is Success -> getProductMapper.mapRemoteModelToUiModel(it.data)
                    is Fail -> ProductInputModel()
                }
            }
            addSource(detailInputModel) {
                productInputModel.value = productInputModel.value.apply { this?.detailInputModel = it }
            }
        }
    }

    fun getProductId(): String {
        return productId.value ?: ""
    }

    fun getDraftId(): Long {
        if (draftId.value != "") {
            return draftId.value?.toLong() ?: 0
        }
        return 0
    }

    fun setProductId(id: String) {
        productId.value = id
    }

    fun setDraftId(id: String) {
        draftId.value = id
    }

    fun updateProductPhotos(imagePickerResult: ArrayList<String>) {
        productInputModel.value?.detailInputModel?.imageUrlOrPathList = imagePickerResult
        this.mImageUrlOrPathList.value = imagePickerResult
    }

    fun updateDetailInputModel(detailInputModel: DetailInputModel) {
        this.detailInputModel.value = detailInputModel
    }

    fun updateDescriptionInputModel(descriptionInputModel: DescriptionInputModel) {
        productInputModel.value?.descriptionInputModel = descriptionInputModel
    }

    fun updateVariantInputModel(variantInputModel: ProductVariantInputModel) {
        productInputModel.value?.variantInputModel = variantInputModel
    }

    fun updateShipmentInputModel(shipmentInputModel: ShipmentInputModel) {
        productInputModel.value?.shipmentInputModel = shipmentInputModel
    }

    fun updateProductInputModel(productDraft: ProductDraft) {
        productInputModel.value?.apply {
            val draft = mapDraftToProductInputModel(productDraft)
            variantInputModel = draft.variantInputModel
            detailInputModel = draft.detailInputModel
            descriptionInputModel = draft.descriptionInputModel
            shipmentInputModel = draft.shipmentInputModel
            draftId = draft.draftId
        }
    }

    fun getNewProductInputModel(imageUrlOrPathList: ArrayList<String>): ProductInputModel {
        val detailInputModel = DetailInputModel().apply { this.imageUrlOrPathList = imageUrlOrPathList }
        return ProductInputModel().apply { this.detailInputModel = detailInputModel }
    }

    private fun loadProductData(productId: String) {
        getProduct(productId)
    }

    private fun getProduct(productId: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                getProductUseCase.params = GetProductUseCase.createRequestParams(productId)
                getProductUseCase.executeOnBackground()
            }
            mGetProductResult.value = Success(data)
        }, onError = {
            mGetProductResult.value = Fail(it)
        })
    }

    fun getVariantList(categoryId: String) {
        launchCatchError(block = {
            mProductVariantList.value = Success(withContext(Dispatchers.IO) {
                getProductVariantUseCase.params =
                        GetProductVariantUseCase.createRequestParams(categoryId)
                getProductVariantUseCase.executeOnBackground()
            })
        }, onError = {
            mProductVariantList.value = Fail(it)
        })
    }

    fun validateProductInput(detailInputModel: DetailInputModel): String {
        var errorMessage = ""
        // validate category input
        if (detailInputModel.categoryId.isEmpty() || detailInputModel.categoryId == "0")  {
            errorMessage += resourceProvider.getInvalidCategoryIdErrorMessage() + "\n"
        }

        // validate product name input
        if (detailInputModel.productName.isEmpty())  {
            errorMessage += resourceProvider.getInvalidNameErrorMessage() + "\n"
        }

        // validate images input
        if (detailInputModel.imageUrlOrPathList.isEmpty())  {
            errorMessage += resourceProvider.getInvalidPhotoCountErrorMessage()
        }

        return errorMessage
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
}