package com.tokopedia.otp.cotp.data;

import com.tokopedia.otp.common.network.WsResponse;
import com.tokopedia.otp.cotp.domain.pojo.MakeLoginPojo;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by nisie on 4/25/18.
 */

public interface SQLoginApi {

    @FormUrlEncoded
    @POST(SQLoginUrl.PATH_MAKE_LOGIN)
    Observable<Response<WsResponse<MakeLoginPojo>>> makeLogin(@FieldMap Map<String, Object> params);

}
