package com.tokopedia.sellerorder.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.applink.order.DeeplinkMapperOrder.FILTER_CANCELLATION_REQUEST
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_DATE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_SORT
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ALL_ORDER
import com.tokopedia.sellerorder.filter.domain.usecase.GetSomOrderFilterUseCase
import com.tokopedia.sellerorder.filter.presentation.model.BaseSomFilter
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterDateUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SomFilterViewModel @Inject constructor(dispatcher: SomDispatcherProvider,
                                             private val getSomOrderFilterUseCase: GetSomOrderFilterUseCase) : BaseViewModel(dispatcher.io()) {

    private val _filterResult = MutableLiveData<Result<List<BaseSomFilter>>>()
    val filterResult: LiveData<Result<List<BaseSomFilter>>> = _filterResult

    private val _resetFilterResult = MutableLiveData<Result<List<SomFilterUiModel>>>()
    val resetFilterResult: LiveData<Result<List<SomFilterUiModel>>> = _resetFilterResult

    private val _updateFilterSelected = MutableLiveData<Result<Pair<List<SomFilterChipsUiModel>, String>>>()
    val updateFilterSelected: LiveData<Result<Pair<List<SomFilterChipsUiModel>, String>>> = _updateFilterSelected

    private val _somFilterOrderListParam = MutableLiveData<Result<SomListGetOrderListParam>>()
    val somFilterOrderListParam: LiveData<Result<SomListGetOrderListParam>> = _somFilterOrderListParam

    private var somFilterUiModel = mutableListOf<SomFilterUiModel>()
    private var somListGetOrderListParam: SomListGetOrderListParam = SomListGetOrderListParam()
    private var somFilterDate: SomFilterDateUiModel? = null
    private var isRequestCancelFilterApplied: Boolean = false

    private fun shouldSelectRequestCancelFilter() {
        if (isRequestCancelFilterApplied) {
            val section = somFilterUiModel.find {
                it.nameFilter == FILTER_TYPE_ORDER
            }
            section?.somFilterData?.indexOfFirst {
                it.id == FILTER_CANCELLATION_REQUEST
            }?.let {
                section.somFilterData[it].run {
                    updateFilterManySelected(idFilter, ChipsUnify.TYPE_NORMAL, it)
                    updateParamSom(idFilter)
                }
            }
        }
    }

    private fun updateIsRequestCancelFilterApplied() {
        isRequestCancelFilterApplied = somFilterUiModel.find {
            it.nameFilter == FILTER_TYPE_ORDER
        }?.somFilterData?.find { it.id == FILTER_CANCELLATION_REQUEST }?.isSelected ?: false
    }

    fun setIsRequestCancelFilterApplied(value: Boolean) {
        isRequestCancelFilterApplied = value
    }

    fun isRequestCancelFilterApplied() = isRequestCancelFilterApplied

    fun getSomFilterUiModel() = somFilterUiModel

    fun setSomFilterUiModel(somFilterUiModel: List<SomFilterUiModel>) {
        this.somFilterUiModel.clear()
        this.somFilterUiModel.addAll(somFilterUiModel)
    }

    fun getSomListGetOrderListParam() = somListGetOrderListParam

    fun setSomListGetOrderListParam(somListGetOrderListParam: SomListGetOrderListParam) {
        this.somListGetOrderListParam = somListGetOrderListParam
    }

    fun getSomFilterData(orderStatus: String, date: String) {
        launchCatchError(block = {
            val result = getSomOrderFilterUseCase.execute()
            val somFilterResult = result.filterIsInstance<SomFilterUiModel>()
            somFilterDate = result.filterIsInstance<SomFilterDateUiModel>().firstOrNull()
                    ?: SomFilterDateUiModel(nameFilter = FILTER_DATE)

            if (date.isNotBlank()) {
                somFilterDate?.date = date
            }

            if (somFilterUiModel.isNullOrEmpty()) {
                somFilterUiModel.clear()
                somFilterUiModel.addAll(somFilterResult)
            }

            somFilterUiModel.find { it.nameFilter == FILTER_STATUS_ORDER }?.somFilterData?.map { chips ->
                chips.isSelected = chips.name == orderStatus
            }

            shouldSelectRequestCancelFilter()
            val somFilterVisitable = mutableListOf<BaseSomFilter>()
            somFilterVisitable.addAll(somFilterUiModel)
            somFilterDate?.let { somFilterVisitable.add(it) }
            somFilterVisitable.let {
                _filterResult.postValue(Success(it))
            }
        }, onError = {
            _filterResult.postValue(Fail(it))
        })
    }

    fun updateFilterSelected(idFilter: String, position: Int, chipType: String) {
        launchCatchError(block = {
            val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
            somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData?.map { it.isSelected = false }
            somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData?.getOrNull(position)?.isSelected = !updateChipsSelected
            val chipsUiModelList = somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData
                    ?: listOf()
            _updateFilterSelected.postValue(Success(Pair(chipsUiModelList, idFilter)))
        }, onError = {
            _updateFilterSelected.postValue(Fail(it))
        })
    }

    fun updateFilterManySelected(idFilter: String, chipType: String, position: Int) {
        launchCatchError(block = {
            val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
            val isSelected = somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData?.getOrNull(position)
            somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData?.onEach { chipsFiler ->
                if (chipsFiler == isSelected) {
                    chipsFiler.isSelected = !updateChipsSelected
                }
            }
            val chipsUiModelList = somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData
                    ?: listOf()
            updateIsRequestCancelFilterApplied()
            _updateFilterSelected.postValue(Success(Pair(chipsUiModelList, idFilter)))
        }, onError = {
            _updateFilterSelected.postValue(Fail(it))
        })
    }

    fun updateSomFilterSeeAll(idFilter: String,
                              somSubFilterList: List<SomFilterChipsUiModel>) {
        launchCatchError(block = {
            somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData = somSubFilterList
            val chipsUiModelList = somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData
                    ?: listOf()
            _updateFilterSelected.postValue(Success(Pair(chipsUiModelList, idFilter)))
        }, onError = {
            _updateFilterSelected.postValue(Fail(it))
        })
    }

    fun updateParamSom(idFilter: String) {
        launchCatchError(block = {
            val idOneSelect = somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData?.find { it.isSelected }?.id
            val idManySelect = somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData?.filter { it.isSelected }?.map { it.id }
                    ?: listOf()
            when (idFilter) {
                FILTER_SORT -> {
                    somListGetOrderListParam.sortBy = idOneSelect ?: 0
                }
                FILTER_STATUS_ORDER -> {
                    val statusList = somFilterUiModel.find { it.nameFilter == idFilter }
                            ?.somFilterData?.filter {
                                it.isSelected
                            }?.flatMap { it.idList } ?: listOf()
                    somListGetOrderListParam.statusList = statusList
                }
                FILTER_TYPE_ORDER -> {
                    somListGetOrderListParam.orderTypeList = idManySelect
                }
                FILTER_COURIER -> {
                    somListGetOrderListParam.shippingList = idManySelect
                }
                FILTER_LABEL -> {
                    somListGetOrderListParam.isShippingPrinted = idOneSelect ?: 0
                }
            }
            _somFilterOrderListParam.postValue(Success(somListGetOrderListParam))
        }, onError = {
            _somFilterOrderListParam.postValue(Fail(it))
        })
    }

    fun resetFilterSelected() {
        launchCatchError(block = {
            somFilterUiModel.map { somFilter ->
                somFilter.somFilterData.map { chips ->
                    chips.isSelected = false
                }
            }
            somListGetOrderListParam = SomListGetOrderListParam()
            _resetFilterResult.postValue(Success(somFilterUiModel))
        }, onError = {
            _resetFilterResult.postValue(Fail(it))
        })
    }
}