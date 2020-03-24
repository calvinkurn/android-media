package com.tokopedia.contactus.inboxticket2.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.data.model.ChipGetInboxDetail
import com.tokopedia.contactus.inboxticket2.data.model.ChipInboxDetails
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class InboxOptionUseCase @Inject constructor(var context: Context){
    fun createRequestParams(id: String?): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString("caseID", id)
        return requestParams
    }

    suspend fun getChipInboxDetail(requestParams: RequestParams):ChipGetInboxDetail?{
        return BaseRepository().getGQLData(GraphqlHelper.loadRawString(context.resources,
                R.raw.inbox_question_query),ChipInboxDetails::class.java, requestParams.parameters).chipGetInboxDetail
    }


}