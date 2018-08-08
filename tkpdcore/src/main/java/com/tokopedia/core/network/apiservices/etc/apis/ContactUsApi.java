package com.tokopedia.core.network.apiservices.etc.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * ContactUsApi
 * Created by Angga.Prasetiyo on 07/12/2015.
 */
public interface ContactUsApi {

    @GET(TkpdBaseURL.ContactUs.PATH_GET_SOLUTION)
    Observable<Response<TkpdResponse>> getSolution(@Path("id") String id);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ContactUs.PATH_CREATE_STEP_2)
    Observable<Response<TkpdResponse>> createTicket(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ContactUs.PATH_CREATE_STEP_1)
    Observable<Response<TkpdResponse>> createTicketValidation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ContactUs.PATH_COMMENT_RATING)
    Observable<Response<TkpdResponse>> commentRating(@FieldMap Map<String, String> params);
}
