package com.tokopedia.buyerorder.detail.domain

import com.tokopedia.buyerorder.detail.data.DetailsData
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationDigiPersoResponse
import com.tokopedia.buyerorder.detail.domain.queries.QueryDigiPerso
import com.tokopedia.buyerorder.detail.domain.queries.QueryOmsDetails
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * created by @bayazidnasir on 19/8/2022
 */

class OrderDetailsUseCase @Inject constructor(
    private val multiUseCase: MultiRequestGraphqlUseCase,
    private val userSession: UserSessionInterface
) {

    suspend fun execute(orderId: String, orderCategory: String, upstream: String): Result<DetailsData>{

        multiUseCase.clearRequest()

        return try {
            multiUseCase.addRequest(createOmsRequest(orderId, orderCategory, upstream))
            multiUseCase.addRequest(createDigiPersoRequest())

            val graphqlResponse = multiUseCase.executeOnBackground()
            val errors = graphqlResponse.getError(DetailsData::class.java)
            if (errors != null && errors.isNotEmpty() && errors[0].extensions != null){
                Fail(MessageErrorException(errors[0].message, errors[0].extensions.code.toString()))
            }else{
                val response = graphqlResponse.getData<DetailsData>(DetailsData::class.java)
                Success(response)
            }
        }catch (e: Throwable){
            Fail(e)
        }
    }

    private fun createOmsRequest(orderId: String, orderCategory: String, upstream: String): GraphqlRequest{
        return GraphqlRequest(
            QueryOmsDetails(),
            DetailsData::class.java,
            createOmsParams(orderId, orderCategory, upstream))
    }

    private fun createDigiPersoRequest(): GraphqlRequest{
        return GraphqlRequest(
            QueryDigiPerso(),
            RecommendationDigiPersoResponse::class.java,
            createDigiPersoParams())
    }

    private fun createOmsParams(orderId: String, orderCategory: String, upstream: String): Map<String, Any>{
        return mapOf(
            OmsKey.ORDER_CATEGORY to orderCategory,
            OmsKey.ORDER_ID to orderId,
            OmsKey.UPSTREAM to upstream,
            OmsKey.DETAIL to DETAIL_ACTION_CODE,
            OmsKey.ACTION to DETAIL_ACTION_CODE,
        )
    }

    private fun createDigiPersoParams(): Map<String, Any>{
        return mapOf(DigiPersoKey.INPUT to mapOf(
                DigiPersoKey.CHANNEL_NAME to DigiPersoKey.DIGI_PERSO_CHANNEL_NAME,
                DigiPersoKey.CLIENT_NUMBERS to listOf(userSession.phoneNumber),
                DigiPersoKey.DG_CATEGORY_IDS to emptyList<Int>(),
                DigiPersoKey.PG_CATEGORY_IDS to emptyList<Int>()
            ))
    }

    companion object{
        private object OmsKey{
            const val ORDER_CATEGORY = "orderCategoryStr"
            const val ORDER_ID = "orderId"
            const val DETAIL = "detail"
            const val ACTION = "action"
            const val UPSTREAM = "upstream"
        }

        private object DigiPersoKey{
            const val INPUT = "input"
            const val CHANNEL_NAME = "channelName"
            const val CLIENT_NUMBERS = "clientNumbers"
            const val DG_CATEGORY_IDS = "dgCategoryIDs"
            const val PG_CATEGORY_IDS = "pgCategoryIDs"
            const val DIGI_PERSO_CHANNEL_NAME = "dg_order_detail_recommendation"
        }

        private const val DETAIL_ACTION_CODE = 1
    }
}