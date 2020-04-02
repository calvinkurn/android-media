package com.tokopedia.product.addedit.preview.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.preview.data.source.api.param.GetProductV3Param
import com.tokopedia.product.addedit.preview.data.source.api.param.OptionV3
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.domain.GetProductUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductPreviewViewModel @Inject constructor(
        private val getProductUseCase: GetProductUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val productId = MutableLiveData<String>()

    private val mImageUrlOrPathList = MutableLiveData<MutableList<String>>()
    val imageUrlOrPathList: LiveData<MutableList<String>> get() = mImageUrlOrPathList

    var productData: Product? = null

    // observing the product id, and will become true if product id exist
    val isEditMode = Transformations.map(productId) { id ->
        !id.isNullOrBlank()
    }

    // observing the product id, and will execute the use case when product id is changed
    private val mGetProductResult = MediatorLiveData<Result<Product>>().apply {
        addSource(productId) {
            if (!productId.value.isNullOrBlank()) loadProductData(it)
        }
    }
    val getProductResult: LiveData<Result<Product>> get() = mGetProductResult

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

    fun setProductId(id: String) {
        productId.value = id
    }

    fun updateProductPhotos(imageUrlOrPathList: ArrayList<String>) {
        this.mImageUrlOrPathList.value = imageUrlOrPathList
    }

    private fun loadProductData(productId: String) {
        getProduct(productId)
    }

    private fun getProduct(productId: String) {
        launchCatchError(block = {
            mGetProductResult.value = Success(withContext(Dispatchers.IO) {
                val options = OptionV3(edit = true, variant = true)
                val getProductV3Param = GetProductV3Param(productId, options)
                getProductUseCase.params = GetProductUseCase.createRequestParams(getProductV3Param)
                getProductUseCase.executeOnBackground()
            })
        }, onError = {
            mGetProductResult.value = Fail(it)
        })
    }
}