package com.tokopedia.talk_old.common.data

import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.talk_old.addtalk.domain.CreateTalkPojo
import com.tokopedia.talk_old.common.domain.pojo.BaseActionTalkPojo
import com.tokopedia.talk_old.common.domain.pojo.InboxTalkPojo
import com.tokopedia.talk_old.producttalk.domain.pojo.ProductTalkPojo
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
            Observable<Response<DataResponse<ProductTalkPojo>>>

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

    @GET(TalkUrl.PATH_GET_SHOP_TALK)
    fun getShopTalk(@QueryMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<InboxTalkPojo>>>

}