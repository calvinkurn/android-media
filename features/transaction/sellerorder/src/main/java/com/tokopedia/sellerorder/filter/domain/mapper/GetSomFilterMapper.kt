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
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel

object GetSomFilterMapper {

    fun mapToSomFilterUiModel(data: SomFilterResponse): List<SomFilterUiModel> {
        return mutableListOf<SomFilterUiModel>().apply {
            add(SomFilterUiModel(FILTER_SORT, mapToFilterSortUiModel(), false, isDividerVisible = true))
            add(SomFilterUiModel(FILTER_STATUS_ORDER, mapToFilterStatusUiModel(data.orderFilterSom.statusList), true, isDividerVisible = true))
            add(SomFilterUiModel(FILTER_TYPE_ORDER, mapToFilterTypeUiModel(data.orderTypeList), true, isDividerVisible = true))
            add(SomFilterUiModel(FILTER_COURIER, mapToFilterCourierUiModel(data.orderFilterSom.shippingList), true, isDividerVisible = true))
            add(SomFilterUiModel(FILTER_LABEL, mapToFilterLabelUiModel(), false, isDividerVisible = true))
            add(SomFilterUiModel(FILTER_DEADLINE, mapToFilterDeadlineUiModel(), isDividerVisible = true))
            add(SomFilterUiModel(FILTER_DATE, canSelectMany = false, isDividerVisible = false))
        }
    }

    private fun mapToFilterSortUiModel(): List<SomFilterChipsUiModel> {
        return mutableListOf<SomFilterChipsUiModel>().apply {
            add(SomFilterChipsUiModel(name = CHIPS_SORT_DESC, id = SORT_DESCENDING))
            add(SomFilterChipsUiModel(name = CHIPS_SORT_ASC, id = SORT_ASCENDING))
        }
    }

    private fun mapToFilterDeadlineUiModel(): List<SomFilterChipsUiModel> {
        return mutableListOf<SomFilterChipsUiModel>().apply {
            add(SomFilterChipsUiModel(name = TODAY_LABEL, id = TODAY))
            add(SomFilterChipsUiModel(name = TOMORROW_LABEL, id = TOMORROW))
            add(SomFilterChipsUiModel(name = TODAY_TOMORROW_LABEL, id = TODAY_TOMORROW))
        }
    }

    private fun mapToFilterLabelUiModel(): List<SomFilterChipsUiModel> {
        return mutableListOf<SomFilterChipsUiModel>().apply {
            add(SomFilterChipsUiModel(name = NOT_YET_PRINTED_LABEL, id = NOT_YET_PRINTED))
            add(SomFilterChipsUiModel(name = ALREADY_PRINT_LABEL, id = ALREADY_PRINT))
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
                            childStatus.text.orEmpty(), childStatus.isChecked))
                }
                add(SomFilterChipsUiModel(idListStatus, name = it.orderStatus, isSelected = false, childStatus = childStatusUiModel))
            }
        }
    }

    private fun mapToFilterCourierUiModel(shippingList: List<SomFilterResponse.OrderFilterSom.Shipping>): List<SomFilterChipsUiModel> {
        return mutableListOf<SomFilterChipsUiModel>().apply {
            shippingList.map {
                add(SomFilterChipsUiModel(id = it.shippingId, name = it.shippingName.orEmpty()))
            }
        }
    }

    private fun mapToFilterTypeUiModel(typeList: List<SomFilterResponse.OrderType>): List<SomFilterChipsUiModel> {
        return mutableListOf<SomFilterChipsUiModel>().apply {
            typeList.map {
                add(SomFilterChipsUiModel(id = it.id, name = it.name.orEmpty()))
            }
        }
    }
}