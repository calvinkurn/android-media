package com.tokopedia.test.application.environment.network;

import android.database.Observable;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WSLogoutService {
    @FormUrlEncoded
    @POST("v4/session/logout.pl")
    Observable<LogoutPojo> logout(@FieldMap Map<String, Object> params);
}
