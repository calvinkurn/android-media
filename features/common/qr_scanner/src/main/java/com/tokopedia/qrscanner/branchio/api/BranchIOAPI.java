package com.tokopedia.qrscanner.branchio.api;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.qrscanner.branchio.BranchIOAndroidDeepLink;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by sandeepgoyal on 19/03/18.
 */

public interface BranchIOAPI {


    @GET()
    Observable<Response<DataResponse<BranchIOAndroidDeepLink>>> getCampaign(@Url String url, @QueryMap Map<String, Object> param);


}
