package com.tokopedia.contactus.inboxticket2.domain.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.contactus.inboxticket2.data.InboxEndpoint
import com.tokopedia.contactus.inboxticket2.domain.InboxDataResponse
import com.tokopedia.contactus.inboxticket2.domain.StepTwoResponse
import com.tokopedia.core.network.constants.TkpdBaseURL
import java.util.*
import javax.inject.Inject

class PostMessageUseCase2 @Inject internal constructor() {

    suspend fun getInboxDataResponse(queryMap: MutableMap<String, Any>): InboxDataResponse<*>? {
        return (BaseRepository().getRestData(
                url,
                object : TypeToken<InboxDataResponse<StepTwoResponse>>() {}.type,
                RequestType.POST,
                queryMap) as InboxDataResponse<StepTwoResponse>)

    }

    private val url: String
        get() = TkpdBaseURL.BASE_CONTACT_US + InboxEndpoint.SEND_MESSAGE_ATTACHMENT

    fun setQueryMap(fileUploaded: String, postKey: String): MutableMap<String, Any> {
        val queryMap: MutableMap<String, Any> = HashMap()
        queryMap.clear()
        queryMap["file_uploaded"] = fileUploaded
        queryMap["post_key"] = postKey
        return queryMap
    }


}