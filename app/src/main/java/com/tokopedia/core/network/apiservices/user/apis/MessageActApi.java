package com.tokopedia.core.network.apiservices.user.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface MessageActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_ARCHIVE_MESSAGES)
    Observable<Response<TkpdResponse>> archiveMessage(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_UNDO_ARCHIVE_MESSAGES)
    Observable<Response<TkpdResponse>> undoArchiveMessage(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_DELETE_MESSAGES)
    Observable<Response<TkpdResponse>> deleteMessage(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_UNDO_DELETE_MESSAGES)
    Observable<Response<TkpdResponse>> undoDeleteMessage(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_MOVE_TO_INBOX)
    Observable<Response<TkpdResponse>> moveToInbox(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_UNDO_MOVE_TO_INBOX)
    Observable<Response<TkpdResponse>> undoMoveToInbox(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_DELETE_MESSAGES_FOREVER)
    Observable<Response<TkpdResponse>> deleteForever(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_ARCHIVE_MESSAGE_DETAIL)
    Observable<Response<TkpdResponse>> archiveMessageDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_DELETE_FOREVER_MESSAGE_DETAIL)
    Observable<Response<TkpdResponse>> deleteForeverMessageDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_DELETE_MESSAGE_DETAIL)
    Observable<Response<TkpdResponse>> deleteMessageDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_FLAG_SPAM)
    Observable<Response<TkpdResponse>> flagSpam(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_MOVE_TO_INBOX_DETAIL)
    Observable<Response<TkpdResponse>> moveToInboxDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_REPLY_MESSAGE)
    Observable<Response<TkpdResponse>> replyMessage(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_SEND_MESSAGE)
    Observable<Response<TkpdResponse>> sendMessage(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_UNDO_ARCHIVE_MESSAGE_DETAIL)
    Observable<Response<TkpdResponse>> undoArchiveMessageDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_UNDO_DELETE_MESSAGE_DETAIL)
    Observable<Response<TkpdResponse>> undoDeleteMessageDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_UNDO_FLAG_SPAM)
    Observable<Response<TkpdResponse>> undoFlagSpam(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_UNDO_MOVE_TO_INBOX_DETAIL)
    Observable<Response<TkpdResponse>> undoMoveToInboxDetail(@FieldMap Map<String, String> params);

}
