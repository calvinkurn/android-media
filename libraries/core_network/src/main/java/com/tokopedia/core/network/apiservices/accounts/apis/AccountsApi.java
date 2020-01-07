package com.tokopedia.core.network.apiservices.accounts.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * AccountsApi
 * Created by stevenfredian on 5/25/16.
 */

@Deprecated
public interface AccountsApi {

    @GET(TkpdBaseURL.Accounts.PATH_DISCOVER_LOGIN)
    Observable<Response<TkpdResponse>> discoverLogin(@QueryMap TKPDMapParam<String, Object> parameters);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.GENERATE_HOST)
    Observable<GeneratedHost> generateHost(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.FCM.UPDATE_FCM)
    Call<String> gcmUpdate(@FieldMap TKPDMapParam<String, Object> param);
}
