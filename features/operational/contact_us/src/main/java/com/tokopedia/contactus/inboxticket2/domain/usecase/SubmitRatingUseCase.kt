package com.tokopedia.contactus.inboxticket2.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.data.model.ChipGetInboxDetail
import com.tokopedia.contactus.inboxticket2.data.model.ChipInboxDetails
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SubmitRatingUseCase @Inject constructor(var context: Context) {

    fun createRequestParams(commentID: String?, rating: String, reason: String?): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString("commentID", commentID)
        requestParams.putInt("rating", rating.toInt())
        requestParams.putString("reason", reason)
        return requestParams
    }


    suspend fun getChipInboxDetail(requestParams: RequestParams):ChipGetInboxDetail?{
       return BaseRepository().getGQLData(GraphqlHelper.loadRawString(context.resources,
                R.raw.submit_rating),ChipInboxDetails::class.java, requestParams.parameters).chipGetInboxDetail
    }

}