package com.tokopedia.sellerorder.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_DATE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_DEADLINE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_SORT
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
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

    private val _updateFilterSelected = MutableLiveData<List<BaseSomFilter>>()
    val updateFilterSelected: LiveData<List<BaseSomFilter>> = _updateFilterSelected

    private val _somFilterUiModelData = MutableLiveData<SomListGetOrderListParam>()
    val somFilterUiModelData: LiveData<SomListGetOrderListParam> = _somFilterUiModelData

    private var somFilterUiModel: List<SomFilterUiModel> = mutableListOf()
    private var somListGetOrderListParam: SomListGetOrderListParam = SomListGetOrderListParam()
    private var somFilterDate: SomFilterDateUiModel? = null

    fun getSomFilterUiModel() = somFilterUiModel

    fun setSomFilterUiModel(somFilterUiModel: List<SomFilterUiModel>) {
        this.somFilterUiModel = somFilterUiModel
    }

    fun getSomListGetOrderListParam() = somListGetOrderListParam

    fun setSomListGetOrderListParam(somListGetOrderListParam: SomListGetOrderListParam) {
        this.somListGetOrderListParam = somListGetOrderListParam
    }

    fun getSomFilterData(orderStatus: String, date: String) {
        launchCatchError(block = {
            val result = getSomOrderFilterUseCase.execute()
            val somFilterResult = result.filterIsInstance<SomFilterUiModel>()
            if(somFilterDate != null) {
                somFilterDate?.date = date
            } else {
                somFilterDate = result.filterIsInstance<SomFilterDateUiModel>().firstOrNull() ?: SomFilterDateUiModel(nameFilter = FILTER_DATE)
            }
            if (somFilterUiModel.isNullOrEmpty()) {
                somFilterUiModel = somFilterResult
            }
            somFilterUiModel.find { it.nameFilter == FILTER_STATUS_ORDER }?.somFilterData =
                    somFilterResult.find { it.nameFilter == FILTER_STATUS_ORDER }?.somFilterData?.onEach { chips ->
                        if (chips.name == orderStatus) {
                            chips.isSelected = true
                        }
                    } ?: listOf()
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

    fun updateFilterSelected(idFilter: String, position: Int, chipType: String, date: String) {
        val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
        somFilterUiModel.find {
            it.nameFilter == idFilter
        }?.somFilterData?.map {
            it.isSelected = false
        }
        if(idFilter != FILTER_STATUS_ORDER) {
            somFilterUiModel.find {
                it.nameFilter == idFilter
            }?.somFilterData?.getOrNull(position)?.isSelected = !updateChipsSelected
        } else {
            somFilterUiModel.find {
                it.nameFilter == idFilter
            }?.somFilterData?.getOrNull(position)?.isSelected = true
        }
        val somFilterVisitable = mutableListOf<BaseSomFilter>()
        somFilterDate?.date = date
        somFilterVisitable.addAll(somFilterUiModel)
        somFilterDate?.let { somFilterVisitable.add(it) }
        _updateFilterSelected.postValue(somFilterUiModel)
    }

    fun updateFilterManySelected(idFilter: String, chipType: String, position: Int, date: String) {
        val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
        val isSelected = somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData?.getOrNull(position)
        somFilterUiModel.find {
            it.nameFilter == idFilter
        }?.somFilterData?.onEach {
            if (it == isSelected) {
                it.isSelected = !updateChipsSelected
            }
        }
        val somFilterVisitable = mutableListOf<BaseSomFilter>()
        somFilterDate?.date = date
        somFilterVisitable.addAll(somFilterUiModel)
        somFilterDate?.let { somFilterVisitable.add(it) }
        _updateFilterSelected.postValue(somFilterUiModel)
    }

    fun updateSomFilterSeeAll(idFilter: String,
                              somSubFilterList: List<SomFilterChipsUiModel>,
                              filterDate: String ) {
        val somFilterVisitable = mutableListOf<BaseSomFilter>()
        somFilterDate?.date = filterDate
        somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData = somSubFilterList
        somFilterVisitable.addAll(somFilterUiModel)
        somFilterDate?.let { somFilterVisitable.add(it) }
        _updateFilterSelected.postValue(somFilterUiModel)
    }

    fun updateParamSom(idFilter: String) {
        val idOneSelect = somFilterUiModel.find {
            it.nameFilter == idFilter
        }?.somFilterData?.find { it.isSelected }?.id

        val idManySelect = somFilterUiModel.firstOrNull { it.nameFilter == idFilter }
                ?.somFilterData?.filter { it.isSelected }?.map { it.id } ?: listOf()

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
            FILTER_TYPE_ORDER -> somListGetOrderListParam.orderTypeList = idManySelect
            FILTER_COURIER -> somListGetOrderListParam.shippingList = idManySelect
            FILTER_LABEL -> somListGetOrderListParam.isShippingPrinted = idOneSelect ?: 0
            FILTER_DEADLINE -> somListGetOrderListParam.deadline = idOneSelect ?: 0
        }
        _somFilterUiModelData.postValue(somListGetOrderListParam)
    }

    fun resetFilterSelected(orderStatus: String) {
        somFilterUiModel.map {
            it.somFilterData.onEach { chips ->
                chips.isSelected = chips.name == orderStatus
            }
        }
        somListGetOrderListParam = SomListGetOrderListParam()
        val somFilterVisitable = mutableListOf<BaseSomFilter>()
        somFilterVisitable.addAll(somFilterUiModel)
        somFilterDate?.let { somFilterVisitable.add(it) }
        _updateFilterSelected.postValue(somFilterVisitable)
    }
}