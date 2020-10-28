package com.tokopedia.sellerorder.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_DEADLINE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_SORT
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
import com.tokopedia.sellerorder.filter.domain.usecase.GetSomOrderFilterUseCase
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SomFilterViewModel @Inject constructor(dispatcher: SomDispatcherProvider,
                                             private val getSomOrderFilterUseCase: GetSomOrderFilterUseCase) : BaseViewModel(dispatcher.io()) {

    private val _filterResult = MutableLiveData<Result<List<SomFilterUiModel>>>()
    val filterResult: LiveData<Result<List<SomFilterUiModel>>> = _filterResult

    private val _updateFilterSelected = MutableLiveData<List<SomFilterUiModel>>()
    val updateFilterSelected: LiveData<List<SomFilterUiModel>> = _updateFilterSelected

    private val _somFilterUiModelData = MutableLiveData<SomListGetOrderListParam>()
    val somFilterUiModelData: LiveData<SomListGetOrderListParam> = _somFilterUiModelData

    private var somFilterUiModel: List<SomFilterUiModel>? = null
    private var somListGetOrderListParam: SomListGetOrderListParam? = null

    fun getSomFilterUiModel() = somFilterUiModel

    fun setSomListGetOrderListParam(somListGetOrderListParam: SomListGetOrderListParam) {
        this.somListGetOrderListParam = somListGetOrderListParam
    }

    fun getSomFilterData(orderStatus: String) {
        launchCatchError(block = {
            val result = getSomOrderFilterUseCase.execute()
            somFilterUiModel = result
            somFilterUiModel?.find { it.nameFilter == FILTER_STATUS_ORDER }?.somFilterData =
                    result.find { it.nameFilter == FILTER_STATUS_ORDER }?.somFilterData?.onEach { chips ->
                        if (chips.name == orderStatus) {
                            chips.isSelected = true
                        }
                    } ?: listOf()
            somFilterUiModel?.let {
                _filterResult.postValue(Success(it))
            }
        }, onError = {
            _filterResult.postValue(Fail(it))
        })
    }

    fun updateFilterSelected(idFilter: String, position: Int, chipType: String) {
        val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
        somFilterUiModel?.find {
            it.nameFilter == idFilter
        }?.somFilterData?.map {
            it.isSelected = false
        }
        somFilterUiModel?.firstOrNull {
            it.nameFilter == idFilter
        }?.somFilterData?.getOrNull(position)?.isSelected = !updateChipsSelected

        _updateFilterSelected.postValue(somFilterUiModel)
    }

    fun updateFilterManySelected(idFilter: String, chipType: String, position: Int) {
        val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
        val isSelected = somFilterUiModel?.find { it.nameFilter == idFilter }?.somFilterData?.get(position)
        somFilterUiModel?.find {
            it.nameFilter == idFilter
        }?.somFilterData?.onEach {
            if (it == isSelected) {
                it.isSelected = !updateChipsSelected
            }
        }

        _updateFilterSelected.postValue(somFilterUiModel)
    }

    fun updateParamSom(idFilter: String) {
        val idOneSelect = somFilterUiModel?.find {
            it.nameFilter == idFilter
        }?.somFilterData?.find { it.isSelected }?.id

        val idManySelect = somFilterUiModel?.firstOrNull { it.nameFilter == idFilter }
                ?.somFilterData?.filter { it.isSelected }?.map { it.id } ?: listOf()

        when (idFilter) {
            FILTER_SORT -> {
                somListGetOrderListParam?.sortBy = idOneSelect ?: 0
            }
            FILTER_STATUS_ORDER -> {
                val statusList = somFilterUiModel?.find { it.nameFilter == idFilter }
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

    fun resetFilterSelected() {
        somFilterUiModel?.forEach {
            it.somFilterData.onEach { chips ->
                chips.isSelected = false
            }
        }
        somListGetOrderListParam = SomListGetOrderListParam()
        _updateFilterSelected.postValue(somFilterUiModel)
    }
}