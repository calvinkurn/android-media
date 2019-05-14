package com.tokopedia.tkpd.network;

import com.tokopedia.sessioncommon.data.model.GetUserInfoData;

import retrofit2.http.GET;
import rx.Observable;

public interface AccountService {
    @GET("info")
    Observable<GetUserInfoData> getInfo();
}
