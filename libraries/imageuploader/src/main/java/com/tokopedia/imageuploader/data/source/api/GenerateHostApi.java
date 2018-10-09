package com.tokopedia.imageuploader.data.source.api;

import com.tokopedia.imageuploader.data.entity.GenerateHostModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public interface GenerateHostApi {

    @FormUrlEncoded
    @POST("v4/action/generate-host/generate_host.pl")
    Observable<Response<GenerateHostModel>> generateHost(@FieldMap Map<String, String> params);
}
