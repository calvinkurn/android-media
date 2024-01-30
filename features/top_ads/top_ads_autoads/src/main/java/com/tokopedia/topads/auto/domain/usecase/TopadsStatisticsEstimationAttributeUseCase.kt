package com.tokopedia.topads.auto.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.auto.data.network.response.EstimationResponse
import com.tokopedia.topads.auto.domain.query.TopadsStatisticsEstimationAttributeGql
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopadsStatisticsEstimationAttributeUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val userSession: UserSessionInterface,
) : GraphqlUseCase<EstimationResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(TopadsStatisticsEstimationAttributeGql)
        setTypeClass(EstimationResponse::class.java)
    }

    suspend fun execute(type: Int, source: String): EstimationResponse {
        setRequestParams(createRequestParam(type, source).parameters)
        return executeOnBackground()
    }

    private fun createRequestParam(type: Int, source: String): RequestParams {
        val requestParam = RequestParams.create()
        requestParam.putString(ParamObject.SHOP_ID, userSession.shopId)
        requestParam.putInt(ParamObject.TYPE, type)
        requestParam.putString(ParamObject.SOURCE, source)
        return requestParam
    }
}
