package com.tokopedia.attachproduct.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.attachproduct.data.model.mapper.mapToListProduct
import com.tokopedia.attachproduct.domain.model.mapper.toDomainModelMapper
import com.tokopedia.attachproduct.domain.usecase.NewAttachProductUseCase
import com.tokopedia.attachproduct.view.presenter.NewAttachProductContract
import com.tokopedia.attachproduct.view.subscriber.AttachProductGetProductListSubscriber
import com.tokopedia.attachproduct.view.uimodel.NewAttachProductItemUiModel
import com.tokopedia.usecase.coroutines.*
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AttachProductViewModel @Inject constructor
    (private val useCaseNew: NewAttachProductUseCase, private val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.io), NewAttachProductContract.Presenter {

    private val _products = MutableLiveData<Result<List<NewAttachProductItemUiModel>>>()
    private val _cacheList = mutableListOf<NewAttachProductItemUiModel>()
    private val _checkedList = mutableListOf<NewAttachProductItemUiModel>()
    private var _cacheHasNext = false
    val products: LiveData<Result<List<NewAttachProductItemUiModel>>>
        get() = _products

    val cacheList: List<NewAttachProductItemUiModel>
        get() = _cacheList

    val cacheHasNext: Boolean
        get() = _cacheHasNext



    override fun loadProductData(query: String, shopId: String, page: Int, warehouseId: String) {
        launchCatchError(block = {
            val result = useCaseNew(generateParam(query, shopId, page, warehouseId))

            withContext(dispatcher.main) {
                val resultModel = result.mapToListProduct().toDomainModelMapper()
                _products.value = Success(resultModel)

                if (query.isEmpty()) {
                    cacheData()
                }
            }
        }, onError = {
            _products.value = Fail(it)
        })
    }


    override fun updateCheckedList(productNews: List<NewAttachProductItemUiModel>, onUpdated: (Int) -> Unit) {
        if (_checkedList.isNotEmpty()) {
            resetCheckedList()
        }
        _checkedList.addAll(productNews)
        onUpdated.invoke(_checkedList.size)
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
                product.productName
            )
        }
        val resultProduct = arrayListOf<ResultProduct>()
        resultProduct.addAll(products)
        onFinish.invoke(resultProduct)
    }

    private fun generateParam(query: String, shopId: String,
                              page: Int, warehouseId: String): HashMap<String, Any> {

        return hashMapOf<String, Any>().apply {
            put(PARAM, "device=android&source=shop_product&rows=$ROW&q=$query&shop_id=" +
                    "$shopId&start=${(page * ROW) - ROW}&user_warehouseId=$warehouseId")
        }
    }

    fun clearCache() {
        if (_cacheList.isNotEmpty()) {
            _cacheList.clear()
        }
    }

    private fun cacheData(){
        val result = _products.value as Success
        val listData = result.data.toMutableList()
        _cacheHasNext = false
        if (result.data.size >= DEFAULT_ROWS) {
            _cacheHasNext = true
            listData.removeAt(result.data.size - 1)
        }
        _cacheList.addAll(listData)
    }

    companion object {
        const val ROW = 11
        const val PARAM = "params"
        const val DEFAULT_ROWS = 10
    }
}
