package com.tokopedia.attachproduct.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.attachproduct.data.model.mapper.mapToListProduct
import com.tokopedia.attachproduct.domain.model.mapper.toDomainModelMapper
import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase
import com.tokopedia.attachproduct.view.presenter.AttachProductContract
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel
import com.tokopedia.usecase.coroutines.*
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class AttachProductViewModel @Inject constructor
    (private val useCase: AttachProductUseCase, private val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main), AttachProductContract.Presenter {

    private val _products = MutableLiveData<Result<List<AttachProductItemUiModel>>>()
    private val _cacheList = mutableListOf<AttachProductItemUiModel>()
    private val _checkedList = mutableListOf<AttachProductItemUiModel>()
    private var _cacheHasNext = false
    private val _checkedListMutableLiveData = MutableLiveData<List<AttachProductItemUiModel>>()

    val checkedList: LiveData<List<AttachProductItemUiModel>>
        get() = _checkedListMutableLiveData
    val products: LiveData<Result<List<AttachProductItemUiModel>>>
        get() = _products

    val cacheList: List<AttachProductItemUiModel>
        get() = _cacheList

    val cacheHasNext: Boolean
        get() = _cacheHasNext

    override fun loadProductData(query: String, shopId: String, page: Int, warehouseId: String) {
        launchCatchError(block = {
            val result = useCase(generateParam(query, shopId, page, warehouseId))

            val resultModel = result.mapToListProduct().toDomainModelMapper()
            _products.value = Success(resultModel)

            if (query.isEmpty()) {
                _products.value.let { data ->
                    if (data != null) {
                        cacheData(data)
                    }
                }
            }
        }, onError = {
            _products.value = Fail(it)
        })
    }

    override fun updateCheckedList(products: List<AttachProductItemUiModel>) {
        if (_checkedList.isNotEmpty()) {
            resetCheckedList()
        }
        _checkedList.addAll(products)
        _checkedListMutableLiveData.value = _checkedList
    }

    override fun resetCheckedList() {
        _checkedList.clear()
    }

    override fun completeSelection(onFinish: (ArrayList<ResultProduct>) -> Unit) {
        val products = _checkedList.map { product ->
            ResultProduct(
                product.productId,
                product.productUrl,
                product.productImage,
                product.productPrice,
                product.productName,
                product.originalPrice,
                product.discountPercentage,
                product.isFreeOngkirActive,
                product.imgUrlFreeOngkir,
                product.stock,
                product.isSupportVariant
            )
        }
        val resultProduct = arrayListOf<ResultProduct>()
        resultProduct.addAll(products)
        onFinish.invoke(resultProduct)
    }

    private fun generateParam(query: String, shopId: String,
                              page: Int, warehouseId: String): HashMap<String, Any> {
        val start = (page * ROW) - ROW
        return hashMapOf<String, Any>().apply {
            put(PARAM, "device=android&source=shop_product&rows=$ROW&q=$query&shop_id=" +
                    "$shopId&start=$start&user_warehouseId=$warehouseId")
        }
    }

    fun clearCache() {
        if (_cacheList.isNotEmpty()) {
            _cacheList.clear()
        }
    }

    private fun cacheData(result: Result<List<AttachProductItemUiModel>>){
        if (result is Success) {
            val listData = result.data.toMutableList()
            _cacheHasNext = false
            if (result.data.size >= DEFAULT_ROWS) {
                _cacheHasNext = true
                listData.removeAt(result.data.size - 1)
            }
            _cacheList.addAll(listData)
        }
    }

    companion object {
        const val ROW = 11
        const val PARAM = "params"
        const val DEFAULT_ROWS = 10
    }
}
