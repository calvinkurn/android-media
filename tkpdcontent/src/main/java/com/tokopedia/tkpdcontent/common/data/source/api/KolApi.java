package com.tokopedia.tkpdcontent.common.data.source.api;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.tkpdcontent.feature.profile.data.pojo.ProfileKolData;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by milhamj on 06/02/18.
 */

public interface KolApi {
    @POST
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<ProfileKolData>>> getProfileKolData(@Body String requestBody);
}
