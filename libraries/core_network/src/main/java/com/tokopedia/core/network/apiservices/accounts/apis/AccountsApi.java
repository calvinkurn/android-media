package com.tokopedia.core.network.apiservices.accounts.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * AccountsApi
 * Created by stevenfredian on 5/25/16.
 */

@Deprecated
public interface AccountsApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.GENERATE_HOST)
    Observable<GeneratedHost> generateHost(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.FCM.UPDATE_FCM)
    Call<String> gcmUpdate(@FieldMap TKPDMapParam<String, Object> param);
}
