package com.tokopedia.contactus.inboxticket2.domain.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.contactus.inboxticket2.data.InboxEndpoint
import com.tokopedia.contactus.inboxticket2.domain.InboxDataResponse
import com.tokopedia.contactus.orderquery.data.CreateTicketResult
import com.tokopedia.core.network.constants.TkpdBaseURL
import java.util.*
import javax.inject.Inject

class PostMessageUseCase @Inject constructor(){

    suspend fun getCreateTicketResult(queryMap: MutableMap<String, Any>): InboxDataResponse<*>? {
        return (BaseRepository().getRestData(
                url,
                object : TypeToken<InboxDataResponse<CreateTicketResult>>() {}.type,
                RequestType.POST,
                queryMap) as InboxDataResponse<CreateTicketResult>)

    }

    private val url: String
         get() = TkpdBaseURL.BASE_CONTACT_US + InboxEndpoint.SEND_MESSAGE

    fun setQueryMap(id: String, message: String, photo: Int, photoall: String): MutableMap<String, Any>{
        val queryMap: MutableMap<String, Any> = HashMap()
        queryMap["ticket_id"] = id
        queryMap["message"] = message
        if (photo == 1) queryMap["p_photo"] = 1
        if (photoall.isNotEmpty()) queryMap["p_photo_all"] = photoall
        return queryMap
    }


}