package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.domain.model.SomListFilterResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import javax.inject.Inject

class FilterResultMapper @Inject constructor() {
    fun mapResponseToUiModel(resultFilterList: SomListFilterResponse.Data, fromCache: Boolean): SomListFilterUiModel {
        return SomListFilterUiModel(
            statusList = mapStatusList(resultFilterList.orderFilterSom.statusList),
            orderTypeList = mapOrderTypeList(resultFilterList.orderTypeList),
            sortByList = mapSortByList(resultFilterList.orderFilterSom.sortByList),
            fromCache = fromCache
        )
    }

    private fun mapStatusList(statusList: List<SomListFilterResponse.Data.OrderFilterSom.Status>): List<SomListFilterUiModel.Status> {
        return statusList.map {
            SomListFilterUiModel.Status(
                key = it.key,
                status = it.status,
                amount = it.amount,
                id = it.id,
                childStatuses = it.childStatuses.map {
                    SomListFilterUiModel.Status.ChildStatus(
                        key = it.key,
                        status = it.text,
                        amount = it.amount,
                        id = it.id,
                        isChecked = it.isChecked
                    )
                },
                isChecked = it.isChecked
            )
        }.run {
            if (containsAllStatusOrderFilter()) {
                this
            } else {
                ArrayList<SomListFilterUiModel.Status>().apply {
                    includeAllStatusOrderFilter()
                    addAll(this@run)
                }
            }
        }
    }

    private fun mapOrderTypeList(orderTypeList: List<SomListFilterResponse.Data.OrderType>): List<SomListFilterUiModel.OrderType> {
        return orderTypeList.map {
            SomListFilterUiModel.OrderType(
                id = it.id,
                key = it.key,
                name = it.name
            )
        }
    }

    private fun mapSortByList(sortByList: List<SomListFilterResponse.Data.OrderFilterSom.SortBy>): List<SomListFilterUiModel.SortBy> {
        return sortByList.map {
            SomListFilterUiModel.SortBy(
                id = it.value
            )
        }
    }

    private fun List<SomListFilterUiModel.Status>.containsAllStatusOrderFilter(): Boolean {
        return any { it.key == SomConsts.STATUS_ALL_ORDER }
    }

    private fun ArrayList<SomListFilterUiModel.Status>.includeAllStatusOrderFilter() {
        add(
            SomListFilterUiModel.Status(
                key = SomConsts.STATUS_ALL_ORDER,
                status = SomConsts.STATUS_NAME_ALL_ORDER
            )
        )
    }
}
