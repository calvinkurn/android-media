package com.tokopedia.cart.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopGroupSimplifiedGqlResponse
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetCartRevampV3UseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository,
                                                 private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<CartData>() {

    private var params: Map<String, Any>? = null

    fun setParams(cartId: String, state: Int) {
        params = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_SELECTED_CART_ID to cartId,
                PARAM_KEY_ADDITIONAL to mapOf(
                        ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress(),
                        PARAM_KEY_STATE to state
                )
        )
    }

    override suspend fun executeOnBackground(): CartData {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val request = GraphqlRequest(getQueryCartRevampV3(), ShopGroupSimplifiedGqlResponse::class.java, params)
        val response = graphqlRepository.response(listOf(request)).getSuccessData<ShopGroupSimplifiedGqlResponse>()

        if (response.shopGroupSimplifiedResponse.status == "OK") {
            return response.shopGroupSimplifiedResponse.data
        } else {
            throw ResponseErrorException(response.shopGroupSimplifiedResponse.errorMessages.joinToString(", "))
        }
    }

    companion object {
        const val PARAM_KEY_LANG = "lang"
        const val PARAM_KEY_SELECTED_CART_ID = "selected_cart_id"
        const val PARAM_KEY_ADDITIONAL = "additional_params"
        const val PARAM_KEY_STATE = "state"

        const val PARAM_VALUE_ID = "id"
    }

}