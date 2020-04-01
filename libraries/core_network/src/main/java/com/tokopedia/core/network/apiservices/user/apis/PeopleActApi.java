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

@Deprecated
public interface PeopleActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_DELETE_ADDRESS)
    Observable<Response<TkpdResponse>> deleteAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_ADDRESS)
    Observable<Response<TkpdResponse>> editAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_DEFAULT_ADDRESS)
    Observable<Response<TkpdResponse>> editDefaultAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_NOTIFICATION)
    Observable<Response<TkpdResponse>> editNotification(@FieldMap Map<String, String> params);

}
