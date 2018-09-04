package com.tokopedia.talk.common.data

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.talk.common.domain.InboxTalkPojo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

/**
 * @author by nisie on 9/3/18.
 */
interface TalkApi {

    @GET(TalkUrl.PATH_GET_INBOX_TALK)
    fun getInboxTalk(@QueryMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<InboxTalkPojo>>>
}