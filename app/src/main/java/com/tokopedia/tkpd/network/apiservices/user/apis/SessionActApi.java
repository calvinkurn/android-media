package com.tokopedia.tkpd.network.apiservices.user.apis;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;

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
public interface SessionActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.REGISTER_NEW)
    Observable<Response<TkpdResponse>> doRegister(@FieldMap Map<String, String> params);
}
