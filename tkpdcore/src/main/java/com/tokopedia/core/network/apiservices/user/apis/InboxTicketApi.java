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
public interface InboxTicketApi {
    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_GET_INBOX_TICKET)
    Observable<Response<TkpdResponse>> getInboxTicket(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_GET_INBOX_TICKET_DETAIL)
    Observable<Response<TkpdResponse>> getInboxTicketDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_GET_INBOX_TICKET_VIEW_MORE)
    Observable<Response<TkpdResponse>> getInboxTicketMore(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_GET_OPEN_TICKET_FORM)
    Observable<Response<TkpdResponse>> getOpenTicketForm(@FieldMap Map<String, String> params);
}
