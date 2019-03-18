package com.tokopedia.affiliate.feature.createpost.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.data.pojo.CheckQuotaQuery
import com.tokopedia.affiliate.feature.createpost.TYPE_AFFILIATE
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.FeedContentResponse
import com.tokopedia.affiliate.feature.createpost.domain.entity.GetContentFormDomain
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 9/26/18.
 */
class GetContentFormUseCase @Inject internal constructor(
        @ApplicationContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase): UseCase<GetContentFormDomain>() {

    override fun createObservable(requestParams: RequestParams): Observable<GetContentFormDomain> {
        graphqlUseCase.clearRequest()

        val query = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_af_content_form
        )
        val request = GraphqlRequest(query, FeedContentResponse::class.java, requestParams.parameters)
        graphqlUseCase.addRequest(request)

        if (requestParams.getString(PARAM_TYPE, "") == TYPE_AFFILIATE) {
            val queryQouta = GraphqlHelper.loadRawString(
                    context.resources,
                    R.raw.query_af_quota
            )
            val requestQouta = GraphqlRequest(queryQouta, CheckQuotaQuery::class.java)
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

        fun createRequestParams(relatedIds: MutableList<String>, type: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PARAM_RELATED_ID, relatedIds)
            requestParams.putString(PARAM_TYPE, type)
            return requestParams
        }
    }

}
