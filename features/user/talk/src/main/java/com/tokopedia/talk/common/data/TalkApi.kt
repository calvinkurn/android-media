package com.tokopedia.talk.common.data

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.talk.addtalk.domain.CreateTalkPojo
import com.tokopedia.talk.common.domain.InboxTalkPojo
import com.tokopedia.talk.common.domain.pojo.BaseActionTalkPojo
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkThreadViewModel
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

    @FormUrlEncoded
    @POST(TalkUrl.PATH_DELETE_COMMENT_TALK)
    fun deleteCommentTalk(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<BaseActionTalkPojo>>>

    @FormUrlEncoded
    @POST(TalkUrl.PATH_FOLLOW_TALK)
    fun followUnfollowTalk(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<BaseActionTalkPojo>>>

    @FormUrlEncoded
    @POST(TalkUrl.PATH_MARK_TALK_NOT_FRAUD)
    fun markTalkNotFraud(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<BaseActionTalkPojo>>>

    @FormUrlEncoded
    @POST(TalkUrl.PATH_REPORT_TALK)
    fun reportTalk(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<BaseActionTalkPojo>>>

    @FormUrlEncoded
    @POST(TalkUrl.PATH_CREATE_TALK)
    fun createTalk(@FieldMap parameters: HashMap<String, Any>): Observable<Response<DataResponse<CreateTalkPojo>>>

}