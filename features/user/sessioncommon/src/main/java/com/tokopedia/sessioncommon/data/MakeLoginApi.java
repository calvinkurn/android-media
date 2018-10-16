package com.tokopedia.sessioncommon.data;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.sessioncommon.data.model.MakeLoginPojo;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by nisie on 10/16/18.
 */
public interface MakeLoginApi {

    @FormUrlEncoded
    @POST(SessionCommonUrl.PATH_MAKE_LOGIN)
    Observable<Response<DataResponse<MakeLoginPojo>>> makeLogin(@FieldMap Map<String, Object>
                                                                        params);
}
