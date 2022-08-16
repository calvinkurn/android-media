package com.tokopedia.sellerorder.filter.domain.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.common.util.SomConsts.ALREADY_PRINT
import com.tokopedia.sellerorder.common.util.SomConsts.ALREADY_PRINT_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_DATE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_SORT
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.NOT_YET_PRINTED
import com.tokopedia.sellerorder.common.util.SomConsts.NOT_YET_PRINTED_LABEL
import com.tokopedia.sellerorder.filter.domain.SomFilterResponse
import com.tokopedia.sellerorder.filter.presentation.model.BaseSomFilter
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterDateUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel

object GetSomFilterMapper {

    fun selectOrderStatusFilters(
        somFilterUiModel: MutableList<SomFilterUiModel>,
        statusList: List<Int>
    ) {
        if (statusList.isNotEmpty()) {
            somFilterUiModel.find {
                it.nameFilter == FILTER_STATUS_ORDER
            }?.somFilterData?.forEach { somFilterData ->
                val isTheSameStatusFilter = somFilterData.idList.any { somFilterDataId ->
                    somFilterDataId in statusList
                }
                somFilterData.isSelected = isTheSameStatusFilter
                if (isTheSameStatusFilter) {
                    somFilterData.childStatus.forEach { somFilterDataChild ->
                        somFilterDataChild.isChecked = statusList.contains(
                            somFilterDataChild.childId.firstOrNull()
                        )
                    }
                } else {
                    somFilterData.childStatus.forEach { somFilterDataChild ->
                        somFilterDataChild.isChecked = true
                    }
                }
            }
        }
    }

    fun selectOrderTypeFilters(somFilterUiModel: List<SomFilterUiModel>, preselectedOrderTypeFilters: List<Long>) {
        somFilterUiModel.find {
            it.nameFilter == FILTER_TYPE_ORDER
        }?.somFilterData?.forEach {
            it.isSelected = preselectedOrderTypeFilters.contains(it.id) || it.isSelected
        }
    }

    fun deselectOrderTypeFilters(somFilterUiModel: List<SomFilterUiModel>, orderTypeFilterIds: List<Long>) {
        val orderTypeFilters = somFilterUiModel.find {
            it.nameFilter == FILTER_TYPE_ORDER
        }
        orderTypeFilterIds.forEach { orderTypeFilterId ->
            orderTypeFilters?.somFilterData?.find {
                it.id == orderTypeFilterId
            }?.isSelected = false
        }
    }

    fun selectShippingFilters(
        somFilterUiModel: List<SomFilterUiModel>,
        shippingFilterIds: List<Long>
    ) {
        somFilterUiModel.find {
            it.nameFilter == FILTER_COURIER
        }?.somFilterData?.forEach {
            it.isSelected = shippingFilterIds.contains(it.id) || it.isSelected
        }
    }

    fun deselectShippingFilters(somFilterUiModel: List<SomFilterUiModel>, shippingFilterIds: List<Long>) {
        val shippingFilters = somFilterUiModel.find {
            it.nameFilter == FILTER_COURIER
        }
        shippingFilterIds.forEach { shippingFilterId ->
            shippingFilters?.somFilterData?.find {
                it.id == shippingFilterId
            }?.isSelected = false
        }
    }

    fun selectSortByFilter(somFilterUiModel: MutableList<SomFilterUiModel>, sortBy: Long) {
        somFilterUiModel.find {
            it.nameFilter == FILTER_SORT
        }?.somFilterData?.forEach {
            it.isSelected = it.id == sortBy
        }
    }

    fun mapToSomFilterVisitable(data: SomFilterResponse): List<BaseSomFilter> {
        return mutableListOf<BaseSomFilter>().apply {
            addAll(mapToSomFilterUiModel(data))
            add(SomFilterDateUiModel(nameFilter = FILTER_DATE))
        }
    }

    fun mapToSomFilterUiModel(data: SomFilterResponse): List<SomFilterUiModel> {
        return mutableListOf<SomFilterUiModel>().apply {
            add(SomFilterUiModel(nameFilter = FILTER_SORT, somFilterData = mapToFilterSortUiModel(data), canSelectMany = false, isDividerVisible = true))
            add(SomFilterUiModel(nameFilter = FILTER_STATUS_ORDER, somFilterData = mapToFilterStatusUiModel(data.orderFilterSom.statusList), canSelectMany = true, isDividerVisible = true))
            add(SomFilterUiModel(nameFilter = FILTER_TYPE_ORDER, somFilterData = mapToFilterTypeUiModel(data.orderFilterSom.orderTypeList), canSelectMany = true, isDividerVisible = true))
            add(SomFilterUiModel(nameFilter = FILTER_COURIER, somFilterData = mapToFilterCourierUiModel(data.orderFilterSom.shippingList), canSelectMany = true, isDividerVisible = true))
            add(SomFilterUiModel(nameFilter = FILTER_LABEL, somFilterData = mapToFilterLabelUiModel(), canSelectMany = false, isDividerVisible = true))
        }
    }

    private fun mapToFilterSortUiModel(data: SomFilterResponse): List<SomFilterChipsUiModel> {
        return data.orderFilterSom.sortList.map {
            SomFilterChipsUiModel(name = it.text, key = it.text, id = it.value)
        }
    }

    private fun mapToFilterLabelUiModel(): List<SomFilterChipsUiModel> {
        return mutableListOf<SomFilterChipsUiModel>().apply {
            add(SomFilterChipsUiModel(name = NOT_YET_PRINTED_LABEL, key = NOT_YET_PRINTED_LABEL, id = NOT_YET_PRINTED.toLong()))
            add(SomFilterChipsUiModel(name = ALREADY_PRINT_LABEL, key = ALREADY_PRINT_LABEL, id = ALREADY_PRINT.toLong()))
        }
    }

    private fun mapToFilterStatusUiModel(statusList: List<SomFilterResponse.OrderFilterSom.StatusList>): List<SomFilterChipsUiModel> {
        return mutableListOf<SomFilterChipsUiModel>().apply {
            statusList.map {
                val idListStatus = mutableListOf<Int>()
                val childStatusUiModel = mutableListOf<SomFilterChipsUiModel.ChildStatusUiModel>()
                it.orderStatusId.map { orderStatusId ->
                    idListStatus.add(orderStatusId)
                }
                it.childStatus.map { childStatus ->
                    val idList = mutableListOf<Int>()
                    childStatus.id.map { id ->
                        idList.add(id)
                    }
                    childStatusUiModel.add(SomFilterChipsUiModel.ChildStatusUiModel(idList, childStatus.key.orEmpty(),
                            childStatus.text.orEmpty(), true))
                }
                add(SomFilterChipsUiModel(idListStatus, key = it.key, name = it.orderStatus, amount = it.orderStatusAmount,
                        isSelected = false, childStatus = childStatusUiModel, idFilter = FILTER_STATUS_ORDER))
            }
        }
    }

    private fun mapToFilterCourierUiModel(shippingList: List<SomFilterResponse.OrderFilterSom.Shipping>): List<SomFilterChipsUiModel> {
        return mutableListOf<SomFilterChipsUiModel>().apply {
            shippingList.map {
                add(SomFilterChipsUiModel(id = it.shippingId.toLongOrZero(), key = it.shippingCode.orEmpty(), name = it.shippingName.orEmpty(), idFilter = FILTER_COURIER))
            }
        }
    }

    private fun mapToFilterTypeUiModel(typeList: List<SomFilterResponse.OrderFilterSom.OrderType>): List<SomFilterChipsUiModel> {
        return mutableListOf<SomFilterChipsUiModel>().apply {
            typeList.map {
                add(SomFilterChipsUiModel(id = it.id, key = it.key.orEmpty(), name = it.name.orEmpty(), idFilter = FILTER_TYPE_ORDER))
            }
        }
    }
}