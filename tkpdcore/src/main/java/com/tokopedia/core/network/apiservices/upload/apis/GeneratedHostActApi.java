package com.tokopedia.core.network.apiservices.upload.apis;

import com.tokopedia.core.myproduct.model.GenerateHostModel;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 10/12/2015.
 *         Added by m.normansyah on 22/12/2015, for manual purpose
 */
@Deprecated
public interface GeneratedHostActApi {

    @FormUrlEncoded
    @POST("/v4/action/generate-host/generate_host.pl")
    Observable<GenerateHostModel> generateHost(
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Field("user_id") String userId,// 5
            @Field("device_id") String deviceId, // 6
            @Field("hash") String hash,// 7
            @Field("device_time") String deviceTime,// 8
            @Field("new_add") String newAdd// 9
    );

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_GENERATE_HOST)
    Observable<GenerateHostModel> generateHost2(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_GENERATE_HOST)
    Observable<GeneratedHost> generateHost(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_GENERATE_HOST_V2)
    Observable<Response<TkpdResponse>> generateHost3(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_GENERATE_HOST)
    Observable<Response<TkpdResponse>> generateHost4(@FieldMap Map<String, String> params);
}
