package com.tokopedia.core.session;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by stevenfredian on 12/30/16.
 */

public interface TruecallerApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Truecaller.VERIFY_PHONE)
    Observable<Response<TkpdResponse>> verifyPhone(@FieldMap Map<String, String> params);
}