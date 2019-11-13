package com.tokopedia.kol.common.data.source.api;

import com.tokopedia.abstraction.common.data.model.request.GraphqlRequest;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.kol.feature.comment.data.pojo.get.GetKolCommentData;
import com.tokopedia.kol.feature.comment.data.pojo.delete.DeleteCommentKolGraphql;
import com.tokopedia.kol.feature.comment.data.pojo.send.SendCommentKolGraphql;
import com.tokopedia.kol.feature.post.data.pojo.GetUserKolPostResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by milhamj on 06/02/18.
 */

public interface KolApi {
    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<GetUserKolPostResponse>>>
    getProfileKolData(@Body GraphqlRequest requestBody);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<GetKolCommentData>>>
    getKolComment(@Body GraphqlRequest requestBody);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<DeleteCommentKolGraphql>>>
    deleteKolComment(@Body GraphqlRequest requestBody);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<SendCommentKolGraphql>>>
    sendKolComment(@Body GraphqlRequest requestBody);

}
