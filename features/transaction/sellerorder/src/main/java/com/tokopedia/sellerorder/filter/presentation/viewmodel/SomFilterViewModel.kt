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
    private var somListGetOrderListParam: SomListGetOrderListParam? = null
    private var somFilterDate = SomFilterDateUiModel(nameFilter = FILTER_DATE)

    fun getSomFilterUiModel() = somFilterUiModel

    fun setSomFilterUiModel(somFilterUiModel: List<SomFilterUiModel>) {
        this.somFilterUiModel = somFilterUiModel
    }

    fun getSomListGetOrderListParam() = somListGetOrderListParam

    fun setSomListGetOrderListParam(somListGetOrderListParam: SomListGetOrderListParam) {
        this.somListGetOrderListParam = somListGetOrderListParam
    }

    fun getSomFilterData(orderStatus: String) {
        launchCatchError(block = {
            val result = getSomOrderFilterUseCase.execute()
            val somFilterResult = result.filterIsInstance<SomFilterUiModel>()
            somFilterDate = result.filterIsInstance<SomFilterDateUiModel>().firstOrNull() ?: SomFilterDateUiModel(nameFilter = FILTER_DATE)
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
            somFilterDate.let { somFilterVisitable.add(it) }
            somFilterVisitable.let {
                _filterResult.postValue(Success(it))
            }
        }, onError = {
            _filterResult.postValue(Fail(it))
        })
    }

    fun updateFilterSelected(idFilter: String, position: Int, chipType: String) {
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
        somFilterVisitable.addAll(somFilterUiModel)
        somFilterVisitable.add(somFilterDate)
        _updateFilterSelected.postValue(somFilterVisitable)
    }

    fun updateFilterManySelected(idFilter: String, chipType: String, position: Int) {
        val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
        val isSelected = somFilterUiModel.find { it.nameFilter == idFilter }?.somFilterData?.get(position)
        somFilterUiModel.find {
            it.nameFilter == idFilter
        }?.somFilterData?.onEach {
            if (it == isSelected) {
                it.isSelected = !updateChipsSelected
            }
        }
        val somFilterVisitable = mutableListOf<BaseSomFilter>()
        somFilterVisitable.addAll(somFilterUiModel)
        somFilterVisitable.add(somFilterDate)
        _updateFilterSelected.postValue(somFilterVisitable)
    }

    fun updateParamSom(idFilter: String) {
        val idOneSelect = somFilterUiModel.find {
            it.nameFilter == idFilter
        }?.somFilterData?.find { it.isSelected }?.id

        val idManySelect = somFilterUiModel.firstOrNull { it.nameFilter == idFilter }
                ?.somFilterData?.filter { it.isSelected }?.map { it.id } ?: listOf()

        when (idFilter) {
            FILTER_SORT -> {
                somListGetOrderListParam?.sortBy = idOneSelect ?: 0
            }
            FILTER_STATUS_ORDER -> {
                val statusList = somFilterUiModel.find { it.nameFilter == idFilter }
                        ?.somFilterData?.filter {
                            it.isSelected
                        }?.flatMap { it.idStatus } ?: listOf()
                somListGetOrderListParam?.statusList = statusList
            }
            FILTER_TYPE_ORDER -> somListGetOrderListParam?.orderTypeList = idManySelect
            FILTER_COURIER -> somListGetOrderListParam?.shippingList = idManySelect
            FILTER_LABEL -> somListGetOrderListParam?.isShippingPrinted = idOneSelect ?: 0
            FILTER_DEADLINE -> somListGetOrderListParam?.deadline = idOneSelect ?: 0
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
        somFilterVisitable.add(somFilterDate)
        _updateFilterSelected.postValue(somFilterVisitable)
    }
}