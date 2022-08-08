package com.tokopedia.centralizedpromoold.domain.mapper

import com.tokopedia.centralizedpromoold.domain.model.GetPromotionListResponseOld
import com.tokopedia.centralizedpromo.view.model.Footer
import com.tokopedia.centralizedpromo.view.model.Status
import com.tokopedia.centralizedpromoold.view.model.OnGoingPromoListUiModelOld
import com.tokopedia.centralizedpromoold.view.model.OnGoingPromoUiModelOld
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

class OnGoingPromotionMapperOld @Inject constructor() {
    fun mapDomainDataModelToUiDataModel(data: GetPromotionListResponseOld?): OnGoingPromoListUiModelOld {
        return OnGoingPromoListUiModelOld(
                title = data?.data?.title.orEmpty(),
                items = data?.data?.promotions?.map {
                    OnGoingPromoUiModelOld(
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