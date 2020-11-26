package com.tokopedia.talk.feature.reporttalk.data

import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.talk.feature.reporttalk.domain.pojo.BaseActionTalkPojo
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

/**
 * @author by nisie on 9/3/18.
 */
interface TalkApi {

    @FormUrlEncoded
    @POST(TalkUrl.PATH_REPORT_TALK)
    fun reportTalk(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<BaseActionTalkPojo>>>

}