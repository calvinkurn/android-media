package com.tokopedia.logisticCommon.data.repository

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.query.CustomProductLogisticQuery
import com.tokopedia.logisticCommon.data.response.customproductlogistic.OngkirGetCPLQGLResponse
import com.tokopedia.logisticCommon.domain.param.CPLParam
import javax.inject.Inject

class CustomProductLogisticUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) :
    CoroutineUseCase<CPLParam, OngkirGetCPLQGLResponse>(dispatcher.io) {

    fun getParam(
        shopId: Long,
        productId: Long?,
        cplParam: List<Long>?
    ): CPLParam {
        return CPLParam(
            cplDataParam = CPLParam.CPLDataParam(
                shopId = shopId,
                productId = productId,
                productCpls = cplParam?.joinToString(separator = ",")
            )
        )
    }

    override fun graphqlQuery(): String {
        return CustomProductLogisticQuery.getCPL
    }

    override suspend fun execute(params: CPLParam): OngkirGetCPLQGLResponse {
        return gql.request(graphqlQuery(), params)
    }
}
