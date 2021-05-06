package com.tokopedia.loginregister.common.data;


import com.tokopedia.loginregister.discover.pojo.DiscoverPojo;
import com.tokopedia.network.data.model.response.DataResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by nisie on 10/11/18.
 */
public interface LoginRegisterApi {

    @GET(LoginRegisterUrl.PATH_DISCOVER_LOGIN)
    Observable<Response<DataResponse<DiscoverPojo>>> discoverLogin(@QueryMap Map<String, Object> parameters);
}
