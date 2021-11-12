package com.tokopedia.product.manage.feature.violation.domain.mapper

import com.tokopedia.product.manage.feature.violation.data.ViolationReasonDetailResponse
import com.tokopedia.product.manage.feature.violation.view.uimodel.ViolationReasonUiModel
import javax.inject.Inject

class ViolationReasonMapper @Inject constructor() {

    fun mapViolationResponseToUiModel(response: ViolationReasonDetailResponse): ViolationReasonUiModel {
        return with(response.detail) {
            ViolationReasonUiModel(
                title = title,
                descTitle = content.description.descDetail,
                descReason = content.description.descInfo,
                stepTitle = content.resolution.resolutionInfo,
                stepList = content.resolution.resolutionSteps,
                buttonText = content.ctaList.getOrNull(0)?.buttonText.orEmpty(),
                buttonApplink = content.ctaList.getOrNull(0)?.buttonLink.orEmpty()
            )
        }
    }

}