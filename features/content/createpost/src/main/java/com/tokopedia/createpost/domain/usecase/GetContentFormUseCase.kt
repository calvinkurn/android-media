package com.tokopedia.createpost.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliatecommon.analytics.CheckQuotaQuery
import com.tokopedia.createpost.common.TYPE_AFFILIATE
import com.tokopedia.createpost.common.data.pojo.getcontentform.FeedContentResponse
import com.tokopedia.createpost.data.raw.GQL_QUERY_CONTENT_FORM
import com.tokopedia.createpost.common.di.ActivityContext
import com.tokopedia.createpost.domain.entity.GetContentFormDomain
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 9/26/18.
 */
class GetContentFormUseCase @Inject constructor(
        @ActivityContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase) : UseCase<GetContentFormDomain>() {

    override fun createObservable(requestParams: RequestParams): Observable<GetContentFormDomain> {
        graphqlUseCase.clearRequest()

        val query = GQL_QUERY_CONTENT_FORM
        val request = GraphqlRequest(query, FeedContentResponse::class.java, requestParams.parameters)
        graphqlUseCase.addRequest(request)

        if (requestParams.getString(PARAM_TYPE, "") == TYPE_AFFILIATE) {
            val queryQouta = GraphqlHelper.loadRawString(
                    context.resources,
                    com.tokopedia.affiliatecommon.R.raw.query_af_quota
            )
            val requestQouta = GraphqlRequest(queryQouta, CheckQuotaQuery::class.java, false)
            graphqlUseCase.addRequest(requestQouta)
        }

        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            GetContentFormDomain(
                    it.getData(FeedContentResponse::class.java),
                    it.getData(CheckQuotaQuery::class.java)
            )
        }
    }

    companion object {
        private const val PARAM_TYPE = "type"
        private const val PARAM_RELATED_ID = "relatedID"
        private const val PARAM_POST_ID = "id"
        private const val PARAM_TOKEN = "token"

        fun createRequestParams(relatedIds: MutableList<String>, type: String, postId: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PARAM_RELATED_ID, relatedIds)
            requestParams.putString(PARAM_TYPE, type)
            requestParams.putString(PARAM_POST_ID, postId)
            return requestParams
        }

        fun createRequestParams(token: String, type: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_TOKEN, token)
            requestParams.putString(PARAM_TYPE, type)
            return requestParams
        }
    }

}
