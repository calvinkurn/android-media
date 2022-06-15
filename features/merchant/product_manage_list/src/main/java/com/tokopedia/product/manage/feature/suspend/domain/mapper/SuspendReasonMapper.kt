package com.tokopedia.product.manage.feature.suspend.domain.mapper

import com.tokopedia.product.manage.feature.suspend.data.SuspendReasonDetailResponse
import com.tokopedia.product.manage.feature.suspend.view.uimodel.SuspendReasonUiModel
import javax.inject.Inject

class SuspendReasonMapper @Inject constructor() {

    fun mapSuspendResponseToUiModel(response: SuspendReasonDetailResponse): SuspendReasonUiModel {
        return with(response.checkProductViolation.data.firstOrNull()) {

            SuspendReasonUiModel(
                productID = this?.productID ?: "0",
                infoImpact = (this?.infoImpact ?: listOf()).toString().replaceBracketArray(),
                infoToPrevent = (this?.infoToPrevent ?: listOf()).toString().replaceBracketArray(),
                infoReason = (this?.infoReason ?: listOf()).toString().replaceBracketArray(),
                infoToResolve = (this?.infoToResolve ?: listOf()),
                infoFootNote = (this?.infoFootNote ?: listOf()).toString().replaceBracketArray(),
                buttonApplink = this?.urlHelpCenter ?: ""
            )
        }
    }

    private fun String.replaceBracketArray(): String {
        val re = Regex("[^A-Za-z0-9, ]")
        return re.replace(this, "")
    }

}