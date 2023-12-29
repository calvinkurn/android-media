package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.domain.model.SomListFilterResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import javax.inject.Inject

class FilterResultMapper @Inject constructor() {
    fun mapResponseToUiModel(resultFilterList: SomListFilterResponse.Data, fromCache: Boolean): SomListFilterUiModel {
        return SomListFilterUiModel(
            quickFilterList = mapQuickFilterList(resultFilterList.orderFilterSom.quickFilterList),
            statusList = mapStatusList(resultFilterList.orderFilterSom.statusList),
            sortByList = mapSortByList(resultFilterList.orderFilterSom.sortByList),
            fromCache = fromCache,
            highLightedStatusKey = resultFilterList.orderFilterSom.highLightedStatusKey.orEmpty()
        )
    }

    private fun mapQuickFilterList(quickFilterList: List<SomListFilterResponse.Data.OrderFilterSom.QuickFilter>): List<SomListFilterUiModel.QuickFilter> {
        return quickFilterList.map {
            SomListFilterUiModel.QuickFilter(
                id = it.id.toLongOrZero(),
                key = it.key,
                name = it.text,
                type = it.type
            )
        }
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
