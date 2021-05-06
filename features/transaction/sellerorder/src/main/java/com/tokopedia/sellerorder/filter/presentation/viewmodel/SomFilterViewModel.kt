package com.tokopedia.sellerorder.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_DATE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_SORT
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.common.util.Utils.formatDate
import com.tokopedia.sellerorder.filter.domain.mapper.GetSomFilterMapper.getIsRequestCancelApplied
import com.tokopedia.sellerorder.filter.domain.mapper.GetSomFilterMapper.getShouldSelectRequestCancelFilter
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

class SomFilterViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                             private val getSomOrderFilterUseCase: GetSomOrderFilterUseCase) : BaseViewModel(dispatcher.io) {

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
            somFilterUiModel.getShouldSelectRequestCancelFilter(
                    ChipsUnify.TYPE_NORMAL,
                    ::updateFilterManySelected,
                    ::updateParamSom)
        }
    }

    private fun updateIsRequestCancelFilterApplied() {
        isRequestCancelFilterApplied = somFilterUiModel.getIsRequestCancelApplied()
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
                if(chips.name != SomConsts.STATUS_NAME_ALL_ORDER) {
                    chips.isSelected = chips.name == orderStatus
                } else {
                    chips.isSelected
                }
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
            updateIsRequestCancelFilterApplied()
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
            updateIsRequestCancelFilterApplied()
            _updateFilterSelected.postValue(Success(Pair(chipsUiModelList, idFilter)))
        }, onError = {
            _updateFilterSelected.postValue(Fail(it))
        })
    }

    fun updateParamSom(idFilter: String) {
        launchCatchError(block = {
            val idOneSelect = somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData?.find { it.isSelected }?.id
            val idManySelect = somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData?.filter { it.isSelected }?.map { it.id }?.toMutableSet()
                    ?: mutableSetOf()
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
                when(somFilter.nameFilter) {
                    FILTER_SORT -> {
                        somListGetOrderListParam.sortBy = SomConsts.SORT_BY_PAYMENT_DATE_DESCENDING
                    }
                    FILTER_STATUS_ORDER -> {
                        somListGetOrderListParam.statusList = emptyList()
                    }
                    FILTER_TYPE_ORDER -> {
                        somListGetOrderListParam.orderTypeList = mutableSetOf()
                    }
                    FILTER_COURIER -> {
                        somListGetOrderListParam.shippingList = mutableSetOf()
                    }
                    FILTER_LABEL -> {
                        somListGetOrderListParam.isShippingPrinted = 0
                    }
                }
            }
            somListGetOrderListParam.startDate = Utils.getNPastMonthTimeText(3)
            somListGetOrderListParam.endDate = Utils.getNowTimeStamp().formatDate(SomConsts.PATTERN_DATE_PARAM)
            _resetFilterResult.postValue(Success(somFilterUiModel))
            _somFilterOrderListParam.postValue(Success(somListGetOrderListParam))
        }, onError = {
            _resetFilterResult.postValue(Fail(it))
        })
    }
}