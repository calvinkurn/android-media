package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.tokopedia.contactus.inboxtickets.data.ContactUsRepository
import com.tokopedia.contactus.inboxtickets.data.model.ChipGetInboxDetail
import com.tokopedia.contactus.inboxtickets.data.model.ChipInboxDetails
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject
import javax.inject.Named

const val CASEID = "caseID"
class InboxOptionUseCase @Inject constructor(
    @Named("inbox_question_query") val inboxQuestionQuery: String,
    private val repository: ContactUsRepository
) {

    fun createRequestParams(id: String?): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(CASEID, id)
        return requestParams
    }

    suspend fun getChipInboxDetail(requestParams: RequestParams): ChipGetInboxDetail {
        return repository.getGQLData(
            inboxQuestionQuery,
            ChipInboxDetails::class.java,
            requestParams.parameters
        ).getInboxDetail()
    }
}
