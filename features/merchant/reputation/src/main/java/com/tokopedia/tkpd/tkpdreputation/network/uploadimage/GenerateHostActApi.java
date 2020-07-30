package com.tokopedia.tkpd.tkpdreputation.network.uploadimage;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface GenerateHostActApi {

    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_GENERATE_HOST)
    Observable<Response<TokopediaWsV4Response>> generateHost4(@FieldMap Map<String, String> params);

}