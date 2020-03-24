package com.tokopedia.contactus.inboxticket2.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.data.model.ChipGetInboxDetail
import com.tokopedia.contactus.inboxticket2.data.model.ChipInboxDetails
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class CloseTicketByUserUseCase @Inject constructor(private val context: Context)  {

    fun createRequestParams(caseID: String?, source: String?): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(CASEID, caseID)
        requestParams.putString(SOURCE, source)
        return requestParams
    }


    suspend fun getChipInboxDetail(requestParams: RequestParams): ChipGetInboxDetail? {
        return BaseRepository().getGQLData(GraphqlHelper.loadRawString(context.resources,
                R.raw.close_ticket_by_user),ChipInboxDetails::class.java, requestParams.parameters).chipGetInboxDetail
    }

    companion object {
        private const val CASEID = "caseID"
        private const val SOURCE = "source"
    }

}