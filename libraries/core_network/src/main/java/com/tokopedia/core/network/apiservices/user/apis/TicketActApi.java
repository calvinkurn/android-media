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
public interface TicketActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_GIVE_RATING)
    Observable<Response<TkpdResponse>> giveRating(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_OPEN_TICKET)
    Observable<Response<TkpdResponse>> openTicket(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_REPLY_TICKET_SUBMIT)
    Observable<Response<TkpdResponse>> replyTicketSubmit(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_REPLY_TICKET_VALIDATION)
    Observable<Response<TkpdResponse>> replyTicketValidation(@FieldMap Map<String, String> params);
}
