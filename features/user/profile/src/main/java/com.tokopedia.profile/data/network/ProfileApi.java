package com.tokopedia.profile.data.network;
import com.tokopedia.abstraction.common.data.model.request.GraphqlRequest;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.profile.data.pojo.ProfileGraphql;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by alvinatin on 19/02/18.
 */

public interface ProfileApi {
    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<ProfileGraphql>>>
    getProfileContent(@Body GraphqlRequest request);
}
