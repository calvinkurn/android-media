package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.sellerorder.list.domain.model.SomListFilterResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import javax.inject.Inject

class FilterResultMapper @Inject constructor() {
    fun mapResponseToUiModel(resultFilterList: SomListFilterResponse.Data.OrderFilterSom, fromCache: Boolean): SomListFilterUiModel {
        return SomListFilterUiModel(statusList = mapStatusList(resultFilterList.statusList), fromCache = fromCache)
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
}