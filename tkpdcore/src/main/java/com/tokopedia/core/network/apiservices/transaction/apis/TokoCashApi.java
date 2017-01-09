package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;
import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by kris on 1/5/17. Tokopedia
 */

public interface TokoCashApi {

    @GET(TkpdBaseURL.TopCash.PATH_WALLET)
    Observable<Response<TopCashItem>> getTokoCash(@QueryMap Map<String, String> params);
}
