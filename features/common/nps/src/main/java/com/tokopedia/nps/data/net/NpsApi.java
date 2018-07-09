package com.tokopedia.nps.data.net;

import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by meta on 28/06/18.
 */
public interface NpsApi {

    @POST(TkpdBaseURL.ContactUs.PATH_FEEDBACK)
    @FormUrlEncoded
    Observable<Response<String>> postFeedback(
            @Field("rating") String rating,
            @Field("category") String category,
            @Field("user_id") String userId,
            @Field("comment") String comment,
            @Field("app_version") String appVersion,
            @Field("device_model") String deviceModel,
            @Field("os_type") String osType,
            @Field("os_version") String osVersion
    );

    @POST(TkpdBaseURL.ContactUs.PATH_FEEDBACK)
    @FormUrlEncoded
    Observable<Response<String>> postFeedback(
            @FieldMap() HashMap<String, String> params
    );
}
