package com.tokopedia.sellerorder.filter.domain.mapper

import com.tokopedia.sellerorder.common.util.SomConsts.ALREADY_PRINT
import com.tokopedia.sellerorder.common.util.SomConsts.ALREADY_PRINT_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.CHIPS_SORT_ASC
import com.tokopedia.sellerorder.common.util.SomConsts.CHIPS_SORT_DESC
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_DATE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_DEADLINE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_SORT
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.NOT_YET_PRINTED
import com.tokopedia.sellerorder.common.util.SomConsts.NOT_YET_PRINTED_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.SORT_ASCENDING
import com.tokopedia.sellerorder.common.util.SomConsts.SORT_DESCENDING
import com.tokopedia.sellerorder.common.util.SomConsts.TODAY
import com.tokopedia.sellerorder.common.util.SomConsts.TODAY_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.TODAY_TOMORROW
import com.tokopedia.sellerorder.common.util.SomConsts.TODAY_TOMORROW_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.TOMORROW
import com.tokopedia.sellerorder.common.util.SomConsts.TOMORROW_LABEL
import com.tokopedia.sellerorder.filter.domain.SomFilterResponse
import com.tokopedia.sellerorder.filter.presentation.model.BaseSomFilter
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterDateUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel

object GetSomFilterMapper {

    fun mapToSomFilterVisitable(data: SomFilterResponse): List<BaseSomFilter> {
        return mutableListOf<BaseSomFilter>().apply {
            addAll(mapToSomFilterUiModel(data))
            add(SomFilterDateUiModel(nameFilter = FILTER_DATE))
        }
    }

    fun mapToSomFilterUiModel(data: SomFilterResponse): List<SomFilterUiModel> {
        return mutableListOf<SomFilterUiModel>().apply {
            add(SomFilterUiModel(nameFilter = FILTER_SORT, somFilterData = mapToFilterSortUiModel(), canSelectMany =  false, isDividerVisible = true))
            add(SomFilterUiModel(nameFilter = FILTER_STATUS_ORDER, somFilterData = mapToFilterStatusUiModel(data.orderFilterSom.statusList), canSelectMany = true, isDividerVisible = true))
            add(SomFilterUiModel(nameFilter = FILTER_TYPE_ORDER, somFilterData = mapToFilterTypeUiModel(data.orderTypeList), canSelectMany =  true, isDividerVisible = true))
            add(SomFilterUiModel(nameFilter = FILTER_COURIER, somFilterData = mapToFilterCourierUiModel(data.orderFilterSom.shippingList), canSelectMany =  true, isDividerVisible = true))
            add(SomFilterUiModel(nameFilter = FILTER_LABEL, somFilterData = mapToFilterLabelUiModel(), canSelectMany = false, isDividerVisible = true))
            add(SomFilterUiModel(nameFilter = FILTER_DEADLINE, somFilterData =  mapToFilterDeadlineUiModel(), canSelectMany = false, isDividerVisible = true))
        }
    }

    private fun mapToFilterSortUiModel(): List<SomFilterChipsUiModel> {
        return mutableListOf<SomFilterChipsUiModel>().apply {
            add(SomFilterChipsUiModel(name = CHIPS_SORT_DESC, key = CHIPS_SORT_DESC, id = SORT_DESCENDING))
            add(SomFilterChipsUiModel(name = CHIPS_SORT_ASC, key = CHIPS_SORT_ASC, id = SORT_ASCENDING))
        }
    }

    private fun mapToFilterDeadlineUiModel(): List<SomFilterChipsUiModel> {
        return mutableListOf<SomFilterChipsUiModel>().apply {
            add(SomFilterChipsUiModel(name = TODAY_LABEL, key = TODAY_LABEL, id = TODAY))
            add(SomFilterChipsUiModel(name = TOMORROW_LABEL, key = TOMORROW_LABEL, id = TOMORROW))
            add(SomFilterChipsUiModel(name = TODAY_TOMORROW_LABEL, key = TODAY_TOMORROW_LABEL, id = TODAY_TOMORROW))
        }
    }

    private fun mapToFilterLabelUiModel(): List<SomFilterChipsUiModel> {
        return mutableListOf<SomFilterChipsUiModel>().apply {
            add(SomFilterChipsUiModel(name = NOT_YET_PRINTED_LABEL, key = NOT_YET_PRINTED_LABEL, id = NOT_YET_PRINTED))
            add(SomFilterChipsUiModel(name = ALREADY_PRINT_LABEL, key = ALREADY_PRINT_LABEL, id = ALREADY_PRINT))
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
                add(SomFilterChipsUiModel(id = it.shippingId, key = it.shippingCode.orEmpty(), name = it.shippingName.orEmpty(), idFilter = FILTER_COURIER))
            }
        }
    }

    private fun mapToFilterTypeUiModel(typeList: List<SomFilterResponse.OrderType>): List<SomFilterChipsUiModel> {
        return mutableListOf<SomFilterChipsUiModel>().apply {
            typeList.map {
                add(SomFilterChipsUiModel(id = it.id, key = it.key.orEmpty(), name = it.name.orEmpty(), idFilter = FILTER_TYPE_ORDER))
            }
        }
    }
}