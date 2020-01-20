package com.tokopedia.loginregister.common.data;


import com.tokopedia.loginregister.activation.domain.pojo.ActionPojo;
import com.tokopedia.loginregister.discover.pojo.DiscoverPojo;
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterValidationPojo;
import com.tokopedia.network.data.model.response.DataResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by nisie on 10/11/18.
 */
public interface LoginRegisterApi {

    @GET(LoginRegisterUrl.PATH_DISCOVER_LOGIN)
    Observable<Response<DataResponse<DiscoverPojo>>> discoverLogin(@QueryMap Map<String, Object>
                                                                           parameters);

    @FormUrlEncoded
    @POST(LoginRegisterUrl.RESEND_ACTIVATION)
    Observable<Response<DataResponse<ActionPojo>>> resendActivation(@FieldMap Map<String, Object>
                                                                            params);

    @FormUrlEncoded
    @POST(LoginRegisterUrl.CHANGE_EMAIL)
    Observable<Response<DataResponse<ActionPojo>>> changeEmail(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(LoginRegisterUrl.PATH_REGISTER_VALIDATION)
    Observable<Response<DataResponse<RegisterValidationPojo>>> validateRegister(@FieldMap Map<String, Object>
                                                                parameters);

}
