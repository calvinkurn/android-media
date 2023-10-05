package com.tokopedia.cart.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopGroupSimplifiedGqlResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.network.exception.ResponseErrorException
import javax.inject.Inject

class GetCartRevampV4UseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetCartParam, CartData>(dispatchers.io) {

    override fun graphqlQuery(): String = CART_REVAMP_V4_QUERY

    @GqlQuery(QUERY_CART_REVAMP_V4, CART_REVAMP_V4_QUERY)
    override suspend fun execute(params: GetCartParam): CartData {
        val request = GraphqlRequest(
            CartRevampV4Query(),
            ShopGroupSimplifiedGqlResponse::class.java,
            mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_SELECTED_CART_ID to params.cartId,
                PARAM_KEY_ADDITIONAL to mapOf(
                    ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress(),
                    PARAM_KEY_STATE to params.state,
                    PARAM_KEY_IS_CART_REIMAGINE to params.isCartReimagine
                )
            )
        )
        val response = graphqlRepository.response(listOf(request))
            .getSuccessData<ShopGroupSimplifiedGqlResponse>()

        if (response.shopGroupSimplifiedResponse.status == "OK") {
            return response.shopGroupSimplifiedResponse.data
        } else {
            throw ResponseErrorException(
                response.shopGroupSimplifiedResponse.errorMessages.joinToString(
                    ", "
                )
            )
        }
    }

    companion object {
        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_KEY_SELECTED_CART_ID = "selected_cart_id"
        private const val PARAM_KEY_ADDITIONAL = "additional_params"
        private const val PARAM_KEY_STATE = "state"
        private const val PARAM_KEY_IS_CART_REIMAGINE = "is_cart_reimagine"

        private const val PARAM_VALUE_ID = "id"

        private const val QUERY_CART_REVAMP_V4 = "CartRevampV4Query"
    }
}

class GetCartParam(
    val cartId: String,
    val state: Int,
    val isCartReimagine: Boolean = false
)
