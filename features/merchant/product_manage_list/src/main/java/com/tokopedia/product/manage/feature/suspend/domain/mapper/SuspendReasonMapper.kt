package com.tokopedia.product.manage.feature.suspend.domain.mapper

import com.tokopedia.product.manage.feature.suspend.data.SuspendReasonDetailResponse
import com.tokopedia.product.manage.feature.suspend.view.uimodel.SuspendReasonUiModel
import javax.inject.Inject

class SuspendReasonMapper @Inject constructor() {

    fun mapSuspendResponseToUiModel(response: SuspendReasonDetailResponse): SuspendReasonUiModel {
        return with(response.checkProductViolation.data[0]) {

            SuspendReasonUiModel(
                productID = productID,
                infoImpact = infoImpact.toString().replaceBracketArray(),
                infoToPrevent = infoToPrevent.toString().replaceBracketArray(),
                infoReason = infoReason.toString().replaceBracketArray(),
                infoToResolve = infoToResolve,
                infoFootNote = infoFootNote.toString().replaceBracketArray(),
                buttonApplink = urlHelpCenter
            )
        }
    }

    private fun String.replaceBracketArray() : String{
        val re = Regex("[^A-Za-z0-9, ]")
        return re.replace(this,"")
    }

}