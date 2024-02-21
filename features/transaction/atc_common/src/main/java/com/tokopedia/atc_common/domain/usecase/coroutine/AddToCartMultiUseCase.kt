package com.tokopedia.atc_common.domain.usecase.coroutine

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcConstant.ATC_ERROR_GLOBAL
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.atc_common.domain.usecase.query.ADD_TO_CART_MULTI_QUERY
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject
import kotlin.math.roundToLong

class AddToCartMultiUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ArrayList<AddToCartMultiParam>, Result<AtcMultiData>>(dispatcher.io) {

    companion object {
        const val QUERY_ADD_TO_CART_MULTI = "AddToCartMultiQuery"
        private const val PARAM_ATC = "param"
    }

    override fun graphqlQuery(): String = ADD_TO_CART_MULTI_QUERY

    @GqlQuery(QUERY_ADD_TO_CART_MULTI, ADD_TO_CART_MULTI_QUERY)
    override suspend fun execute(params: ArrayList<AddToCartMultiParam>): Result<AtcMultiData> {
        val param = mapOf(
            PARAM_ATC to params
        )
        val request = GraphqlRequest(
            AddToCartMultiQuery(),
            AtcMultiData::class.java,
            param
        )
        val response = graphqlRepository.response(listOf(request))
            .getSuccessData<AtcMultiData>()
        if (response.atcMulti.buyAgainData.success == 1) {
            for (param in params) {
                AddToCartBaseAnalytics.sendAppsFlyerTracking(
                    param.productId,
                    param.productName,
                    param.productPrice.roundToLong().toString(),
                    param.qty.toString(),
                    param.category
                )
                AddToCartBaseAnalytics.sendBranchIoTracking(
                    param.productId, param.productName, param.productPrice.roundToLong().toString(),
                    param.qty.toString(), param.category, "",
                    "", "", "",
                    "", "", param.custId,
                    param.shopName
                )
            }
            return Success(response)
        } else {
            throw MessageErrorException(ATC_ERROR_GLOBAL)
        }
    }
}
