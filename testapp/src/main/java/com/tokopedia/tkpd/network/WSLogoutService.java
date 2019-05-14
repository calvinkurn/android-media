package com.tokopedia.tkpd.network;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface WSLogoutService {
    @FormUrlEncoded
    @POST("v4/session/logout.pl")
    Observable<LogoutPojo> logout(@FieldMap Map<String, Object> params);
}
