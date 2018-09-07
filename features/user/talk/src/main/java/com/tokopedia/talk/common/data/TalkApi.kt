package com.tokopedia.talk.common.data

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.talk.common.domain.InboxTalkPojo
import com.tokopedia.talk.common.domain.pojo.BaseActionTalkPojo
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

/**
 * @author by nisie on 9/3/18.
 */
interface TalkApi {

    @GET(TalkUrl.PATH_GET_INBOX_TALK)
    fun getInboxTalk(@QueryMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<InboxTalkPojo>>>

    @GET(TalkUrl.PATH_GET_PRODUCT_TALK)
    fun getProductTalk(@QueryMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<InboxTalkPojo>>>

    @FormUrlEncoded
    @POST(TalkUrl.PATH_DELETE_TALK)
    fun deleteTalk(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<BaseActionTalkPojo>>>
}