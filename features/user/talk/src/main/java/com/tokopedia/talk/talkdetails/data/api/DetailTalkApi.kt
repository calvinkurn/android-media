package com.tokopedia.talk.talkdetails.data.api

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.constant.TkpdBaseURL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

/**
 * Created by Hendri on 05/09/18.
 */

interface DetailTalkApi {

    @GET(TkpdBaseURL.KunyitTalk.GET_COMMENT_TALK)
    fun getTalkComment(@QueryMap params: HashMap<String, Any>): Observable<Response<DataResponse<String>>>

}