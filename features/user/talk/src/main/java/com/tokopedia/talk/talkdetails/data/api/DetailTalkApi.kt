package com.tokopedia.talk.talkdetails.data.api

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.talk.talkdetails.data.SendCommentResponse
import com.tokopedia.talk.talkdetails.domain.pojo.TalkDetailsPojo
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

/**
 * Created by Hendri on 05/09/18.
 */

interface DetailTalkApi {

    @GET(TkpdBaseURL.KunyitTalk.GET_COMMENT_TALK)
    fun getTalkComment(@QueryMap params: HashMap<String, Any>): Observable<Response<DataResponse<TalkDetailsPojo>>>

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitTalk.ADD_COMMENT_TALK)
    fun sendComment(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<SendCommentResponse>>>
}