package com.tokopedia.sessioncommon.data;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.sessioncommon.data.model.GetUserInfoData;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by nisie on 10/12/18.
 */
public interface GetProfileApi {

    @GET(SessionCommonUrl.PATH_GET_INFO)
    Observable<Response<GetUserInfoData>> getInfo(@QueryMap Map<String, Object> params);

}
