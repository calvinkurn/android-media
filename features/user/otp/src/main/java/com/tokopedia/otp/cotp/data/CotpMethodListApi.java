package com.tokopedia.otp.cotp.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.otp.cotp.domain.pojo.ListMethodItemPojo;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by nisie on 4/27/18.
 */

public interface CotpMethodListApi {

    @GET(CotpUrl.PATH_GET_METHOD_LIST)
    Observable<Response<DataResponse<ListMethodItemPojo>>> getVerificationMethodList(
            @QueryMap Map<String, Object> parameters);

}
