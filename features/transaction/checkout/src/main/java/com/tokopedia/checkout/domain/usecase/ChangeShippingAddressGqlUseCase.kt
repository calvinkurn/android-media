package com.tokopedia.checkout.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.data.model.request.changeaddress.DataChangeAddressRequest
import com.tokopedia.checkout.data.model.response.changeshippingaddress.ChangeShippingAddressGqlResponse
import com.tokopedia.checkout.domain.model.changeaddress.SetShippingAddressData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_ERROR_GLOBAL
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import javax.inject.Inject

class ChangeShippingAddressGqlUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<ChangeShippingAddressRequest, SetShippingAddressData>(dispatchers.io) {

    companion object {
        const val CHANGE_SHIPPING_ADDRESS_PARAMS = "CHANGE_SHIPPING_ADDRESS_PARAMS"
        const val PARAM_CARTS = "carts"
        const val PARAM_ONE_CLICK_SHIPMENT = "is_one_click_shipment"
    }

    override fun graphqlQuery(): String {
        return CHANGE_ADDRESS_CART
    }

    override suspend fun execute(params: ChangeShippingAddressRequest): SetShippingAddressData {
        val request = GraphqlRequest(
            graphqlQuery(),
            ChangeShippingAddressGqlResponse::class.java,
            mapOf(
                PARAM_CARTS to params.carts,
                PARAM_ONE_CLICK_SHIPMENT to params.isOcs
            )
        )
        val gqlResponse = graphqlRepository.response(listOf(request)).getData<ChangeShippingAddressGqlResponse>(ChangeShippingAddressGqlResponse::class.java)
        if (gqlResponse != null) {
            return SetShippingAddressData().apply {
                isSuccess = gqlResponse.changeShippingAddressResponse.dataResponse.success == 1
                messages = gqlResponse.changeShippingAddressResponse.dataResponse.messages ?: emptyList()
            }
        } else {
            throw CartResponseErrorException(CART_ERROR_GLOBAL)
        }
    }
}

const val CHANGE_ADDRESS_CART = """
    mutation change_address_cart(${"$"}carts: [ParamsCartAddressUpdateCartType], ${"$"}is_one_click_shipment: Boolean) {
        change_address_cart(carts: ${"$"}carts, is_one_click_shipment: ${"$"}is_one_click_shipment) {
            status
            error_message
            data {
                success
                message
            }
        }
    }
"""

data class ChangeShippingAddressRequest(
    val carts: List<DataChangeAddressRequest>,
    val isOcs: Boolean
)
