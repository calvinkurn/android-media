package com.tokopedia.product.manage.feature.violation.domain.mapper

import com.tokopedia.product.manage.feature.violation.data.ViolationReasonDetailResponse
import com.tokopedia.product.manage.feature.violation.view.uimodel.ViolationReasonUiModel
import javax.inject.Inject

class ViolationReasonMapper @Inject constructor() {

    fun mapViolationResponseToUiModel(response: ViolationReasonDetailResponse): ViolationReasonUiModel {
        return with(response.detail) {
            ViolationReasonUiModel(
                title = mainTitle,
                descTitle = description.preDescText,
                descReason = description.descText,
                stepTitle = "Lakukan langkah berikut untuk menyelesaikan:",
                stepList = resolutionSteps.map { it.htmlText },
                buttonText = callToActionInfo.buttonText,
                buttonApplink = callToActionInfo.buttonLink
            )
        }
    }

}