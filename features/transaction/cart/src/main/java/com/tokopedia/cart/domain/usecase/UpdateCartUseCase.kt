package com.tokopedia.cart.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cart.data.model.request.UpdateCartWrapperRequest
import com.tokopedia.cart.data.model.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.cart.domain.mapper.mapUpdateCartData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-12-26.
 */

class UpdateCartUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<UpdateCartWrapperRequest, UpdateCartData>(dispatcher.io) {

    companion object {
        const val PARAM_UPDATE_CART_REQUEST = "PARAM_UPDATE_CART_REQUEST"

        const val PARAM_KEY_SOURCE = "source"

        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_VALUE_ID = "id"

        const val QUERY_UPDATE_CART = "UpdateCartQuery"
    }

    override fun graphqlQuery(): String = UPDATE_CART_V2_QUERY

    @GqlQuery(QUERY_UPDATE_CART, UPDATE_CART_V2_QUERY)
    override suspend fun execute(params: UpdateCartWrapperRequest): UpdateCartData {
        val param = mapOf(
            PARAM_KEY_LANG to PARAM_VALUE_ID,
            PARAM_UPDATE_CART_REQUEST to params.getUpdateCartRequest(),
            KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress(),
            PARAM_KEY_SOURCE to params.source
        )
        val request = GraphqlRequest(UpdateCartQuery(), UpdateCartGqlResponse::class.java, param)
        val updateCartGqlResponse =
            graphqlRepository.response(listOf(request)).getSuccessData<UpdateCartGqlResponse>()
        return mapUpdateCartData(
            updateCartGqlResponse,
            updateCartGqlResponse.updateCartDataResponse.data
        )
    }
}
