package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.list.domain.model.SomListFilterResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel

object FilterResultMapper {
    fun mapResponseToUiModel(resultFilterList: SomListFilterResponse.Data.OrderFilterSom): SomListFilterUiModel {
        return SomListFilterUiModel(statusList = mapStatusList(resultFilterList.statusList))
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
        }
    }

    fun convertToMapSomListFilterUiModel(data: List<SomFilterUiModel>?,
                                         idFilter: String,
                                         statusList: SomListFilterUiModel?): SomListFilterUiModel {
        return SomListFilterUiModel(statusList = mapFilterStatusList(data?.find { it.nameFilter == idFilter }?.somFilterData, statusList))
    }

    private fun mapFilterStatusList(dataList: List<SomFilterChipsUiModel>?,
                                    statusListOld: SomListFilterUiModel?): List<SomListFilterUiModel.Status> {
        val statusList = mutableListOf<SomListFilterUiModel.Status>()
        dataList?.map {
            var amount = 0
            statusListOld?.statusList?.forEach { oldStatus ->
                if(oldStatus.status == it.name) {
                    amount = oldStatus.amount
                }
            }
            statusList.add(SomListFilterUiModel.Status(
                    key = it.key,
                    status = it.name,
                    id = it.idList,
                    isChecked = it.isSelected,
                    amount = amount,
                    childStatuses = it.childStatus.map { childStatus ->
                        SomListFilterUiModel.Status.ChildStatus(
                                key = childStatus.key,
                                status = childStatus.text,
                                id = childStatus.childId,
                                isChecked = childStatus.isChecked
                        )
                    }
            ))
        }
        return statusList
    }
}