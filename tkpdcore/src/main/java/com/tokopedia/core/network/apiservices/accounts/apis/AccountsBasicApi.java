package com.tokopedia.core.network.apiservices.accounts.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by nisie on 12/27/17.
 */

public interface AccountsBasicApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Accounts.PATH_GET_TOKEN)
    Call<String> getTokenSynchronous(@FieldMap Map<String, String> params);
}
