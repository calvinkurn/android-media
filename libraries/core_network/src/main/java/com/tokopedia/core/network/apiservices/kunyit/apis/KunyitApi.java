package com.tokopedia.core.network.apiservices.kunyit.apis;


import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * KunyitApi
 * Created by stevenfredian on 8/2/16.
 */

@Deprecated
public interface KunyitApi {

    @GET(TkpdBaseURL.KunyitTalk.GET_INBOX_TALK)
    Observable<Response<TkpdResponse>> getInboxTalk(@QueryMap Map<String, String> stringStringMap);

    @GET(TkpdBaseURL.KunyitTalk.GET_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> getProductTalk(@QueryMap Map<String, String> stringStringMap);

    @GET(TkpdBaseURL.KunyitTalk.GET_COMMENT_TALK)
    Observable<Response<TkpdResponse>> getCommentTalk(@QueryMap Map<String, String> stringStringMap);

    @GET(TkpdBaseURL.KunyitTalk.GET_INBOX_TALK_DETAIL)
    Observable<Response<TkpdResponse>> getTalkDetail(@QueryMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitTalk.ADD_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> addProductTalk(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitTalk.ADD_COMMENT_TALK)
    Observable<Response<TkpdResponse>> addCommentTalk(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitTalk.DELETE_COMMENT_TALK)
    Observable<Response<TkpdResponse>> deleteCommentTalk(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitTalk.REPORT_COMMENT_TALK)
    Observable<Response<TkpdResponse>> reportCommentTalk(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitTalk.FOLLOW_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> followProductTalk(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitTalk.DELETE_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> deleteProductTalk(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitTalk.REPORT_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> reportProductTalk(@FieldMap Map<String, String> stringStringMap);

    @GET(TkpdBaseURL.KunyitTalk.GET_SHOP_TALK)
    Observable<Response<TkpdResponse>> getShopTalk(@QueryMap Map<String, String> stringStringMap);

    @GET(TkpdBaseURL.KunyitMessage.GET_INBOX_MESSAGE)
    Observable<Response<TkpdResponse>> getInboxMessage(@QueryMap Map<String, String> stringStringMap);

    @GET(TkpdBaseURL.KunyitMessage.GET_INBOX_DETAIL_MESSAGE)
    Observable<Response<TkpdResponse>> getInboxMessageDetail(@QueryMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.SEND_MESSAGE)
    Observable<Response<TkpdResponse>> sendMessage(@FieldMap Map<String, Object> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.SEND_REPLY_MESSAGE)
    Observable<Response<TkpdResponse>> replyMessage(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.DELETE_MESSAGES)
    Observable<Response<TkpdResponse>> deleteMessage(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.DELETE_MESSAGES_FOREVER)
    Observable<Response<TkpdResponse>> deleteForever(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.UNDO_DELETE_MESSAGES)
    Observable<Response<TkpdResponse>> undoDeleteMessage(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.ARCHIVE_MESSAGES)
    Observable<Response<TkpdResponse>> archiveMessage(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.UNDO_ARCHIVE_MESSAGES)
    Observable<Response<TkpdResponse>> undoArchiveMessage(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.FLAG_SPAM)
    Observable<Response<TkpdResponse>> flagSpam(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.UNDO_FLAG_SPAM)
    Observable<Response<TkpdResponse>> undoFlagSpam(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.MOVE_TO_INBOX)
    Observable<Response<TkpdResponse>> moveToInbox(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.UNDO_MOVE_TO_INBOX)
    Observable<Response<TkpdResponse>> undoMoveToInbox(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.MARK_AS_READ)
    Observable<Response<TkpdResponse>> markAsRead(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.MARK_AS_UNREAD)
    Observable<Response<TkpdResponse>> markAsUnread(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.KunyitMessage.SEND_MESSAGE)
    Observable<Response<TkpdResponse>> sendMessageOld(@FieldMap Map<String, String>
                                                              stringStringMap);


}
