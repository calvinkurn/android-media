package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.tokopedia.contactus.inboxtickets.data.ContactUsRepository
import com.tokopedia.contactus.inboxtickets.data.model.ChipGetInboxDetail
import com.tokopedia.contactus.inboxtickets.data.model.ChipInboxDetails
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject
import javax.inject.Named

class CloseTicketByUserUseCase @Inject constructor(@Named("close_ticket_by_user") val closeTicketQuery: String,
                                                   private val repository: ContactUsRepository
) {

    fun createRequestParams(caseID: String?, source: String?): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(CASEID, caseID)
        requestParams.putString(SOURCE, source)
        return requestParams
    }


    suspend fun getChipInboxDetail(requestParams: RequestParams): ChipGetInboxDetail {
        return repository.getGQLData(closeTicketQuery, ChipInboxDetails::class.java, requestParams.parameters).getInboxDetail()
    }

    companion object {
        private const val CASEID = "caseID"
        private const val SOURCE = "source"
    }

}
