package com.tokopedia.tkpd.network.apiservices.etc.apis;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by nisie on 8/15/16.
 */
public interface ContactUsActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_CREATE_TICKET)
    Observable<Response<TkpdResponse>> createTicket(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_CREATE_TICKET_VALIDATION)
    Observable<Response<TkpdResponse>> createTicketValidation(@FieldMap Map<String, String> params);


}
