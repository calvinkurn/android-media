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
public interface MSISDNApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_GET_VERIFICATION_NUMBER_FORM)
    Observable<Response<TkpdResponse>> getVerificationNumberForm(@FieldMap Map<String, String> params);
}
