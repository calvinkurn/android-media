package com.tokopedia.tkpd.network;

import com.tokopedia.sessioncommon.data.model.TokenViewModel;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface LoginService {
    @FormUrlEncoded
    @POST("token")
    Observable<TokenViewModel> getToken(@FieldMap Map<String, String> map);
}
