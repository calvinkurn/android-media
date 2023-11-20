package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.TopadsInsightProductsResponse
import com.tokopedia.topads.common.domain.query.TopadsInsightProductsQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsInsightProductsUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
    private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TopadsInsightProductsResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(TopadsInsightProductsQuery)
        setTypeClass(TopadsInsightProductsResponse::class.java)
    }

    suspend operator fun invoke(status: String): TopadsInsightProductsResponse {
        setRequestParams(createRequestParam(status).parameters)
        return executeOnBackground()
    }

    private fun createRequestParam(status: String): RequestParams {
        val requestParam = RequestParams.create()
        requestParam.putString(ParamObject.SHOP_id, userSession.shopId)
        requestParam.putObject(ParamObject.STATUS, status)
        return requestParam
    }
}
