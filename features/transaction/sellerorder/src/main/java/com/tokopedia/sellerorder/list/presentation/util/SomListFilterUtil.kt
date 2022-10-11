package com.tokopedia.sellerorder.list.presentation.util

import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

object SomListFilterUtil {

    fun selectSomFilterSortBy(somFilterUiModelList: MutableList<SomFilterUiModel>, sortId: Long) {
        somFilterUiModelList.find {
            it.nameFilter == SomConsts.FILTER_SORT
        }?.somFilterData?.forEach { it.isSelected = it.id == sortId }
    }

    fun updateStatusOrderFilter(
        somFilterUiModelList: MutableList<SomFilterUiModel>,
        ids: List<Int>
    ) {
        somFilterUiModelList.find {
            it.nameFilter == SomConsts.FILTER_STATUS_ORDER
        }?.somFilterData?.forEach {
            val isTheSameStatusFilter = it.idList.any {
                it in ids
            }
            it.isSelected = isTheSameStatusFilter
            if (isTheSameStatusFilter) {
                it.childStatus.forEach {
                    it.isChecked = ids.contains(it.childId.firstOrNull())
                }
            } else {
                it.childStatus.forEach {
                    it.isChecked = true
                }
            }
        }
    }

    fun inferTabActive(
        filterResult: Result<SomListFilterUiModel>?,
        statusList: List<Int>
    ): String? {
        return filterResult?.let {
            if (it is Success) {
                it.data.statusList.find {
                    it.id.containsAll(statusList)
                }?.key
            } else null
        }
    }
}