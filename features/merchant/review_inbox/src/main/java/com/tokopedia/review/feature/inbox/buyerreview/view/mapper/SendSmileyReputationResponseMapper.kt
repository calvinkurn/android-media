package com.tokopedia.review.feature.inbox.buyerreview.view.mapper

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationResponseWrapper
import javax.inject.Inject

class SendSmileyReputationResponseMapper @Inject constructor() {

    companion object {
        private const val SUCCESS_CODE = 1
    }

    fun mapResponseToUiData(
        response: SendSmileyReputationResponseWrapper.Data.Response?
    ): Boolean {
        return response?.success == SUCCESS_CODE
    }
}