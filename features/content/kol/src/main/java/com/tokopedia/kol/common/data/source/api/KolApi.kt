package com.tokopedia.kol.common.data.source.api

import com.tokopedia.kol.common.data.model.request.GraphqlRequest
import com.tokopedia.kol.feature.comment.data.pojo.get.GetKolCommentData
import com.tokopedia.kol.feature.comment.data.pojo.delete.DeleteCommentKolGraphql
import com.tokopedia.kol.feature.comment.data.pojo.send.SendCommentKolGraphql
import com.tokopedia.network.data.model.response.GraphqlResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import rx.Observable

/**
 * @author by milhamj on 06/02/18.
 */
interface KolApi {
    @POST("./")
    @Headers("Content-Type: application/json")
    fun getKolComment(@Body requestBody: GraphqlRequest?): Observable<Response<GraphqlResponse<GetKolCommentData>>>

    @POST("./")
    @Headers("Content-Type: application/json")
    fun deleteKolComment(@Body requestBody: GraphqlRequest?): Observable<Response<GraphqlResponse<DeleteCommentKolGraphql>>>

    @POST("./")
    @Headers("Content-Type: application/json")
    fun sendKolComment(@Body requestBody: GraphqlRequest?): Observable<Response<GraphqlResponse<SendCommentKolGraphql>>>
}
