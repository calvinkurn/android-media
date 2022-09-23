package com.tokopedia.product.manage.feature.suspend.domain.mapper

import com.tokopedia.product.manage.feature.suspend.data.SuspendReasonDetailResponse
import com.tokopedia.product.manage.feature.suspend.view.uimodel.SuspendReasonUiModel
import javax.inject.Inject

class SuspendReasonMapper @Inject constructor() {

    fun mapSuspendResponseToUiModel(response: SuspendReasonDetailResponse): SuspendReasonUiModel {
        return with(response.checkProductViolation.data.firstOrNull()) {

            SuspendReasonUiModel(
                productID = this?.productID ?: "0",
                infoImpact = (this?.infoImpact.orEmpty()).joinToString(separator = ","),
                infoToPrevent = (this?.infoToPrevent.orEmpty()).joinToString(separator = ","),
                infoReason = (this?.infoReason.orEmpty()).joinToString(separator = ","),
                infoToResolve = (this?.infoToResolve.orEmpty()),
                infoFootNote = (this?.infoFootNote.orEmpty()).joinToString(separator = ","),
                buttonApplink = this?.urlHelpCenter ?: ""
            )
        }
    }

}