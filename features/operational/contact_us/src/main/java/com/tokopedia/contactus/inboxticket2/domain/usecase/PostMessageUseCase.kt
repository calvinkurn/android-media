package com.tokopedia.contactus.inboxticket2.domain.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.contactus.inboxticket2.data.ContactUsRepository
import com.tokopedia.contactus.inboxticket2.data.InboxEndpoint
import com.tokopedia.contactus.inboxticket2.domain.InboxDataResponse
import com.tokopedia.contactus.orderquery.data.CreateTicketResult
import com.tokopedia.core.network.constants.TkpdBaseURL
import java.util.*
import javax.inject.Inject

const val TICKET_ID = "ticket_id"
const val MESSAGE = "message"
const val IS_IMAGE = "p_photo"
const val IMAGE_AS_STRING = "p_photo_all"

class PostMessageUseCase @Inject constructor(private val repository: ContactUsRepository){

    suspend fun getCreateTicketResult(queryMap: MutableMap<String, Any>): InboxDataResponse<*>? {
        return (repository.postRestData(
                url,
                object : TypeToken<InboxDataResponse<CreateTicketResult>>() {}.type,
                queryMap) as InboxDataResponse<CreateTicketResult>)

    }

    private val url: String
         get() = TkpdBaseURL.BASE_CONTACT_US + InboxEndpoint.SEND_MESSAGE

    fun setQueryMap(id: String, message: String, photo: Int, photoall: String): MutableMap<String, Any>{
        val queryMap: MutableMap<String, Any> = HashMap()
        queryMap[TICKET_ID] = id
        queryMap[MESSAGE] = message
        if (photo == 1) queryMap[IS_IMAGE] = 1
        if (photoall.isNotEmpty()) queryMap[IMAGE_AS_STRING] = photoall
        return queryMap
    }


}