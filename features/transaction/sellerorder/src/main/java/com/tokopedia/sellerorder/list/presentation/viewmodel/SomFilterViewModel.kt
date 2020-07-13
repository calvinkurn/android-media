package com.tokopedia.sellerorder.list.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.list.data.model.SomListAllFilter
import com.tokopedia.sellerorder.list.domain.filter.SomGetAllFilterUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-09-18.
 */
class SomFilterViewModel @Inject constructor(dispatcher: SomDispatcherProvider,
                                             private val somGetAllFilterUseCase: SomGetAllFilterUseCase) : BaseViewModel(dispatcher.ui()) {

    private val _shippingListResult = MutableLiveData<Result<MutableList<SomListAllFilter.Data.ShippingList>>>()
    val shippingListResult: LiveData<Result<MutableList<SomListAllFilter.Data.ShippingList>>>
        get() = _shippingListResult

    private val _statusOrderListResult = MutableLiveData<Result<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>>>()
    val statusOrderListResult: LiveData<Result<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>>>
        get() = _statusOrderListResult

    private val _orderTypeListResult = MutableLiveData<Result<MutableList<SomListAllFilter.Data.OrderType>>>()
    val orderTypeListResult: LiveData<Result<MutableList<SomListAllFilter.Data.OrderType>>>
        get() = _orderTypeListResult

    fun loadSomFilterData(filterQuery: String) {
        launchCatchError(block = {
            somGetAllFilterUseCase.execute(filterQuery)
            _shippingListResult.postValue(somGetAllFilterUseCase.getShippingListResult())
            _statusOrderListResult.postValue(somGetAllFilterUseCase.getStatusOrderListResult())
            _orderTypeListResult.postValue(somGetAllFilterUseCase.getOrderTypeListResult())
        }, onError = {
            _shippingListResult.postValue(Fail(it))
            _statusOrderListResult.postValue(Fail(it))
            _orderTypeListResult.postValue(Fail(it))
        })
    }

    fun loadSomFilterDataOld(filterQuery: String) {
        // launch { getFilterList(filterQuery) }
    }

    /*suspend fun getFilterList(rawQuery: String) {
        launchCatchError(block = {
            val filterListData = withContext(Dispatchers.IO) {
                val filterRequest = GraphqlRequest(rawQuery, POJO_FILTER_ALL)
                graphqlRepository.getReseponse(listOf(filterRequest))
                        .getSuccessData<SomListAllFilter.Data>()
            }
            _shippingListResult.postValue(Success(filterListData.orderShippingList.toMutableList()))
            _statusOrderListResult.postValue(Success(filterListData.orderFilterSomSingle.statusList.toMutableList()))
            _orderTypeListResult.postValue(Success(filterListData.orderTypeList.toMutableList()))

        }, onError = {
            _shippingListResult.postValue(Fail(it))
            _statusOrderListResult.postValue(Fail(it))
            _orderTypeListResult.postValue(Fail(it))
        })
    }

    companion object {
        private val POJO_FILTER_ALL = SomListAllFilter.Data::class.java
    }*/
}