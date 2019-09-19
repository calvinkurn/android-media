package com.tokopedia.tkpd.network;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.sessioncommon.data.model.MakeLoginPojo;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface WSService {
    @FormUrlEncoded
    @POST("v4/session/make_login.pl")
    Observable<DataResponse<MakeLoginPojo>> makeLogin(@FieldMap Map<String, Object>
                                                                        params);
}
