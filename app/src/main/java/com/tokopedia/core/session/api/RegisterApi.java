package com.tokopedia.core.session.api;

import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.session.model.network.ValidateEmailData;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by m.normansyah on 1/25/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public interface RegisterApi {

    String USER_EMAIL = "user_email";
    String VALIDATE_EMAIL_PL = "validate_email.pl";
    String frenky = "http://www.fs-frenky.ndvl";
    String VALIDATE_EMAIL_FRENKY = "/web-service/v4/action/register/validate_email.pl";

    @GET(VALIDATE_EMAIL_PL)
    Observable<ValidateEmailData> validateEmail(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Query(NetworkCalculator.USER_ID) String userId,// 5
            @Query(NetworkCalculator.DEVICE_ID) String deviceId, // 6
            @Query(NetworkCalculator.HASH) String hash,// 7
            @Query(NetworkCalculator.DEVICE_TIME) String deviceTime,// 8
            @Query(USER_EMAIL) String userEmail// 9
    );

    @FormUrlEncoded
    @POST(VALIDATE_EMAIL_FRENKY)
    Observable<ValidateEmailData> validateEmailDev(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Field(NetworkCalculator.USER_ID) String userId,// 5
            @Field(NetworkCalculator.DEVICE_ID) String deviceId, // 6
            @Field(NetworkCalculator.HASH) String hash,// 7
            @Field(NetworkCalculator.DEVICE_TIME) String deviceTime,// 8
            @Field(USER_EMAIL) String userEmail// 9
    );

}
