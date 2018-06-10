package com.tokopedia.tkpd.deeplink.source.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpd.deeplink.source.entity.BranchIOAndroidDeepLink;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by sandeepgoyal on 19/03/18.
 */

public interface BranchIOAPI {


    @GET()
    Observable<Response<DataResponse<BranchIOAndroidDeepLink>>> getCampaign(@Url String url,@QueryMap Map<String, Object> param);


}
