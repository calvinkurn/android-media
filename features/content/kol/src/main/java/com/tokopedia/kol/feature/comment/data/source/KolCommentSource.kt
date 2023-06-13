package com.tokopedia.kol.feature.comment.data.source

import com.tokopedia.kol.common.data.model.request.GraphqlRequest
import com.tokopedia.kol.common.data.source.api.KolApi
import com.tokopedia.kol.feature.comment.data.mapper.KolDeleteCommentMapper
import com.tokopedia.kol.feature.comment.data.mapper.KolGetCommentMapper
import com.tokopedia.kol.feature.comment.data.mapper.KolSendCommentMapper
import com.tokopedia.kol.feature.comment.data.raw.GQL_MUTATION_CREATE_KOL_COMMENT
import com.tokopedia.kol.feature.comment.data.raw.GQL_MUTATION_DELETE_KOL_COMMENT
import com.tokopedia.kol.feature.comment.data.raw.GQL_QUERY_GET_KOL_COMMENT
import com.tokopedia.kol.feature.comment.domain.model.SendKolCommentDomain
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 11/2/17.
 */
class KolCommentSource @Inject constructor(
    private val kolApi: KolApi,
    private val kolGetCommentMapper: KolGetCommentMapper,
    private val kolDeleteCommentMapper: KolDeleteCommentMapper,
    private val kolSendCommentMapper: KolSendCommentMapper
) {
    fun getComments(requestParams: RequestParams): Observable<KolComments> {
        return kolApi.getKolComment(getRequestPayload(requestParams, GQL_QUERY_GET_KOL_COMMENT))
            .map(kolGetCommentMapper)
    }

    fun sendComment(requestParams: RequestParams): Observable<SendKolCommentDomain> {
        return kolApi.sendKolComment(getRequestPayload(requestParams,
            GQL_MUTATION_CREATE_KOL_COMMENT)).map(kolSendCommentMapper)
    }

    fun deleteKolComment(requestParams: RequestParams): Observable<Boolean> {
        return kolApi.deleteKolComment(getRequestPayload(requestParams,
            GQL_MUTATION_DELETE_KOL_COMMENT)).map(kolDeleteCommentMapper)
    }

    private fun getRequestPayload(
        requestParams: RequestParams,
        rawResource: String
    ): GraphqlRequest {
        return GraphqlRequest(
            rawResource,
            requestParams.parameters
        )
    }
}
