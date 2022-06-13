package com.tokopedia.attachproduct.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.attachproduct.data.model.mapper.mapToListProduct
import com.tokopedia.attachproduct.domain.model.mapper.toDomainModelMapper
import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel
import com.tokopedia.usecase.coroutines.*
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class AttachProductViewModel @Inject constructor
    (private val useCase: AttachProductUseCase, dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

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

    fun loadProductData(query: String, shopId: String, page: Int, warehouseId: String) {
        launchCatchError(block = {
            val start = (page * ROW) - ROW
            val result = useCase(hashMapOf<String, Any>(PARAM to "device=android&source=attach_product&rows=$ROW&q=$query&shop_id=" +
                    "$shopId&start=$start&user_warehouseId=$warehouseId"))
            val resultModel = result.mapToListProduct().toDomainModelMapper()
            if (query.isEmpty()) {
                _products.value = Success(resultModel)
                val dataSuccess = _products.value as Success
                cacheData(dataSuccess)
            } else {
                _products.value = Success(resultModel)
            }
        }, onError = {
            _products.value = Fail(it)
        })
    }

    fun updateCheckedList(products: List<AttachProductItemUiModel>) {
        _checkedList.clear()
        _checkedList.addAll(products)
        _checkedListMutableLiveData.value = _checkedList
    }

    fun clearCache() {
        _cacheList.clear()
    }

    private fun cacheData(result: Success<List<AttachProductItemUiModel>>){
        val listData = result.data.toMutableList()
        if (result.data.size >= DEFAULT_ROWS) {
            _cacheHasNext = true
            listData.removeAt(result.data.size - 1)
        } else {
            _cacheHasNext = false
        }
        _cacheList.addAll(listData)
    }

    companion object {
        const val ROW = 11
        const val PARAM = "params"
        const val DEFAULT_ROWS = 10
    }
}
