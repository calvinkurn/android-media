package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.tokopedia.contactus.inboxtickets.data.ContactUsRepository
import com.tokopedia.contactus.inboxtickets.data.model.ChipGetInboxDetail
import com.tokopedia.contactus.inboxtickets.data.model.ChipInboxDetails
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject
import javax.inject.Named

const val COMMENT_ID = "commentID"
const val CSAT_RATING = "rating"
const val REASON = "reason"

class SubmitRatingUseCase @Inject constructor(@Named("submit_rating") val submitRatingQuery: String,
                                              private val repository: ContactUsRepository
) {

    fun createRequestParams(commentID: String, rating: Int, reason: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(COMMENT_ID, commentID)
        requestParams.putInt(CSAT_RATING, rating)
        requestParams.putString(REASON, reason)
        return requestParams
    }


    suspend fun getChipInboxDetail(requestParams: RequestParams): ChipGetInboxDetail {
        return repository.getGQLData(submitRatingQuery,
            ChipInboxDetails::class.java,
            requestParams.parameters).getInboxDetail()
    }

}
