package com.tokopedia.contactus.inboxticket2.domain.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.contactus.inboxticket2.data.ContactUsRepository
import com.tokopedia.contactus.inboxticket2.data.InboxEndpoint
import com.tokopedia.contactus.inboxticket2.domain.InboxDataResponse
import com.tokopedia.contactus.inboxticket2.domain.StepTwoResponse
import com.tokopedia.core.network.constants.TkpdBaseURL
import java.util.*
import javax.inject.Inject

const val FILE_UPLOADED = "file_uploaded"
const val POST_KEY = "post_key"

class PostMessageUseCase2 @Inject internal constructor(private val repository: ContactUsRepository) {

    suspend fun getInboxDataResponse(queryMap: MutableMap<String, Any>): InboxDataResponse<*>? {
        return (repository.postRestData(
                url,
                object : TypeToken<InboxDataResponse<StepTwoResponse>>() {}.type,
                queryMap) as InboxDataResponse<StepTwoResponse>)

    }

    private val url: String
        get() = TkpdBaseURL.BASE_CONTACT_US + InboxEndpoint.SEND_MESSAGE_ATTACHMENT

    fun setQueryMap(fileUploaded: String, postKey: String): MutableMap<String, Any> {
        val queryMap: MutableMap<String, Any> = HashMap()
        queryMap.clear()
        queryMap[FILE_UPLOADED] = fileUploaded
        queryMap[POST_KEY] = postKey
        return queryMap
    }


}