package com.tokopedia.sellerorder.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_SORT
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.common.util.Utils.formatDate
import com.tokopedia.sellerorder.filter.domain.mapper.GetSomFilterMapper
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
import kotlinx.coroutines.launch
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

    @Throws(NoSuchElementException::class)
    private fun getSomFilterDate(result: List<BaseSomFilter>): SomFilterDateUiModel {
        return result.filterIsInstance<SomFilterDateUiModel>().first()
    }

    private fun getSomFilterUiModel(nameFilter: String): SomFilterUiModel? {
        return somFilterUiModel.find { it.nameFilter == nameFilter }
    }

    private fun getSomFilterUiModelItems(nameFilter: String): List<SomFilterChipsUiModel> {
        return getSomFilterUiModel(nameFilter)?.somFilterData.orEmpty()
    }

    private fun findFirstSelectedFilterUiModelItemId(nameFilter: String): Long {
        return getSomFilterUiModelItems(nameFilter).find {
            it.isSelected
        }?.id.orZero()
    }

    private fun findAllSelectedFilterUiModelItemsIds(nameFilter: String): MutableSet<Long> {
        return getSomFilterUiModelItems(nameFilter).filter {
            it.isSelected
        }.map {
            it.id
        }.toMutableSet()
    }

    private fun updateSomFilterUiModel(
        nameFilter: String,
        somSubFilterList: List<SomFilterChipsUiModel>
    ) {
        getSomFilterUiModel(nameFilter)?.somFilterData = somSubFilterList
    }

    private fun selectPreselectedOrderStatusFilter() {
        GetSomFilterMapper.selectOrderStatusFilters(somFilterUiModel, somListGetOrderListParam.statusList)
    }

    private fun selectPreselectedOrderTypeFilters() {
        GetSomFilterMapper.selectOrderTypeFilters(somFilterUiModel, somListGetOrderListParam.orderTypeList.toList())
    }

    private fun selectPreselectedSortByFilter() {
        GetSomFilterMapper.selectSortByFilter(somFilterUiModel, somListGetOrderListParam.sortBy)
    }

    private fun deselectCorrespondingFilterItems(nameFilter: String) {
        getSomFilterUiModelItems(nameFilter).forEach { it.isSelected = false }
    }

    private fun updateCorrespondingFilterItemSelectedState(
        nameFilter: String,
        position: Int,
        chipType: String
    ) {
        val selected = chipType == ChipsUnify.TYPE_SELECTED
        getSomFilterUiModelItems(nameFilter).getOrNull(position)?.isSelected = !selected
    }

    fun getSomFilterUiModel() = somFilterUiModel

    fun setSomFilterUiModel(somFilterUiModel: List<SomFilterUiModel>) {
        this.somFilterUiModel.clear()
        this.somFilterUiModel.addAll(somFilterUiModel)
    }

    fun getSomListGetOrderListParam() = somListGetOrderListParam

    fun setSomListGetOrderListParam(somListGetOrderListParam: SomListGetOrderListParam) {
        this.somListGetOrderListParam = somListGetOrderListParam
    }

    fun getSomFilterData(date: String) {
        launchCatchError(block = {
            val result = getSomOrderFilterUseCase.execute()
            val somFilterResult = result.filterIsInstance<SomFilterUiModel>()
            val somFilterDate = getSomFilterDate(result)

            if (date.isNotBlank()) {
                somFilterDate.date = date
            }

            if (somFilterUiModel.isEmpty()) {
                somFilterUiModel.addAll(somFilterResult)
            }

            selectPreselectedOrderStatusFilter()
            selectPreselectedOrderTypeFilters()
            selectPreselectedSortByFilter()
            val somFilterVisitable = mutableListOf<BaseSomFilter>()
            somFilterVisitable.addAll(somFilterUiModel)
            somFilterVisitable.add(somFilterDate)
            _filterResult.postValue(Success(somFilterVisitable))
            this@SomFilterViewModel.somFilterDate = somFilterDate
        }, onError = {
            _filterResult.postValue(Fail(it))
        })
    }

    fun updateFilterSelected(idFilter: String, position: Int, chipType: String) {
        launch {
            deselectCorrespondingFilterItems(idFilter)
            updateCorrespondingFilterItemSelectedState(idFilter, position, chipType)
            val chipsUiModelList = getSomFilterUiModelItems(idFilter)
            _updateFilterSelected.postValue(Success(Pair(chipsUiModelList, idFilter)))
        }
    }

    fun updateFilterManySelected(idFilter: String, chipType: String, position: Int) {
        launch {
            updateCorrespondingFilterItemSelectedState(idFilter, position, chipType)
            val chipsUiModelList = getSomFilterUiModelItems(idFilter)
            _updateFilterSelected.postValue(Success(Pair(chipsUiModelList, idFilter)))
        }
    }

    fun updateSomFilterSeeAll(idFilter: String,
                              somSubFilterList: List<SomFilterChipsUiModel>) {
        launch {
            updateSomFilterUiModel(idFilter, somSubFilterList)
            val chipsUiModelList = getSomFilterUiModelItems(idFilter)
            _updateFilterSelected.postValue(Success(Pair(chipsUiModelList, idFilter)))
        }
    }

    fun updateParamSom(idFilter: String) {
        launch {
            val idOneSelect = findFirstSelectedFilterUiModelItemId(idFilter)
            val idManySelect = findAllSelectedFilterUiModelItemsIds(idFilter)
            when (idFilter) {
                FILTER_SORT -> {
                    somListGetOrderListParam.sortBy = idOneSelect
                }
                FILTER_STATUS_ORDER -> {
                    somListGetOrderListParam.statusList = getSomFilterUiModelItems(idFilter).filter {
                        it.isSelected
                    }.flatMap { it.idList }
                }
                FILTER_TYPE_ORDER -> {
                    somListGetOrderListParam.orderTypeList = idManySelect
                }
                FILTER_COURIER -> {
                    somListGetOrderListParam.shippingList = idManySelect
                }
                FILTER_LABEL -> {
                    somListGetOrderListParam.isShippingPrinted = idOneSelect
                }
            }
            _somFilterOrderListParam.postValue(Success(somListGetOrderListParam))
        }
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