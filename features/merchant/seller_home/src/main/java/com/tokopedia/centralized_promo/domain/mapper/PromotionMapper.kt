package com.tokopedia.centralized_promo.domain.mapper

import com.tokopedia.centralized_promo.domain.model.GetPromotionListResponse
import com.tokopedia.centralized_promo.view.model.Footer
import com.tokopedia.centralized_promo.view.model.OnGoingPromoUiModel
import com.tokopedia.centralized_promo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralized_promo.view.model.Status
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

class PromotionMapper @Inject constructor() {
    fun mapRemoteModelToUiModel(data: GetPromotionListResponse?): OnGoingPromoListUiModel {
        return OnGoingPromoListUiModel(
                title = data?.data?.title.orEmpty(),
                promotions = ArrayList(data?.data?.promotions?.map {
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
                }.orEmpty()),
                errorMessage = data?.header?.message?.joinToString("\n").orEmpty()
        )
    }
}