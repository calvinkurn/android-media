package com.tokopedia.centralizedpromo.domain.mapper

import com.tokopedia.centralizedpromo.domain.model.GetPromotionListResponse
import com.tokopedia.centralizedpromo.view.model.Footer
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoUiModel
import com.tokopedia.centralizedpromo.view.model.Status
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

class OnGoingPromotionMapper @Inject constructor() {
    fun mapDomainDataModelToUiDataModel(data: GetPromotionListResponse?): OnGoingPromoListUiModel {
        return OnGoingPromoListUiModel(
                title = data?.data?.title.orEmpty(),
                items = data?.data?.promotions?.map {
                    OnGoingPromoUiModel(
                            title = it.title.orEmpty(),
                            status = Status(
                                    text = it.status?.text.orEmpty(),
                                    count = it.status?.count.orZero(),
                                    url = it.status?.url.orEmpty()
                            ),
                            footer = Footer(
                                    text = it.footer?.text.orEmpty(),
                                    url = it.footer?.url.orEmpty()
                            )
                    )
                }.orEmpty(),
                errorMessage = data?.header?.message?.joinToString("\n").orEmpty()
        )
    }
}
