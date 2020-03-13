package com.tokopedia.purchase_platform.features.one_click_checkout.order.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.features.cart.data.model.request.UpdateCartRequest
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.UpdateCartOccGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.UpdateCartOccRequest
import javax.inject.Inject

class UpdateCartOccUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<UpdateCartOccGqlResponse>) {

    fun execute(param: UpdateCartOccRequest, onSuccess: (UpdateCartOccGqlResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(generateParam(param))
        graphqlUseCase.setTypeClass(UpdateCartOccGqlResponse::class.java)
        graphqlUseCase.execute({ response: UpdateCartOccGqlResponse ->
            if (response.response.status.equals("OK", true)) {
                if (response.response.data.success == 1) {
                    onSuccess(response)
                } else if (response.response.data.messages.isNotEmpty()) {
                    onError(MessageErrorException(response.response.data.messages[0]))
                } else {
                    onError(MessageErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi"))
                }

            } else if (response.response.errorMessage.isNotEmpty()) {
                onError(MessageErrorException(response.response.errorMessage[0]))
            } else {
                onError(MessageErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi"))
            }

        }, { throwable: Throwable ->
            onError(throwable)
        })

    }

    private fun generateParam(param: UpdateCartOccRequest): Map<String, Any?> {
        return mapOf(
                PARAM_KEY to mapOf(
                        PARAM_CART_KEY to listOf(
                                mapOf(
                                        PARAM_CART_ID_KEY to param.cart[0].cartId,
                                        PARAM_QUANTITY_KEY to param.cart[0].quantity,
                                        PARAM_NOTES_KEY to param.cart[0].notes,
                                        PARAM_PRODUCT_ID_KEY to param.cart[0].productId,
                                        PARAM_SHIPPING_ID_KEY to param.cart[0].shippingId,
                                        PARAM_SP_ID_KEY to param.cart[0].spId
                                )
                        ),
                        PARAM_PROFILE_KEY to mapOf(
                                PARAM_PROFILE_ID_KEY to param.profile.profileId,
                                PARAM_GATEWAY_CODE_KEY to param.profile.gatewayCode,
                                PARAM_METADATA_KEY to param.profile.metadata,
                                PARAM_SERVICE_ID_KEY to param.profile.serviceId,
                                PARAM_ADDRESS_ID_KEY to param.profile.addressId
                        )
                )
        )
    }

    companion object {
        const val PARAM_KEY = "update"

        const val PARAM_CART_KEY = "cart"
        const val PARAM_PROFILE_KEY = "profile"

        const val PARAM_CART_ID_KEY = "cart_id"
        const val PARAM_QUANTITY_KEY = "quantity"
        const val PARAM_NOTES_KEY = "notes"
        const val PARAM_PRODUCT_ID_KEY = "product_id"
        const val PARAM_SHIPPING_ID_KEY = "shipping_id"
        const val PARAM_SP_ID_KEY = "sp_id"

        const val PARAM_PROFILE_ID_KEY = "profile_id"
        const val PARAM_GATEWAY_CODE_KEY = "gateway_code"
        const val PARAM_METADATA_KEY = "metadata"
        const val PARAM_SERVICE_ID_KEY = "service_id"
        const val PARAM_ADDRESS_ID_KEY = "address_id"


        val QUERY = """
        mutation update_cart_occ(${"$"}update: OneClickCheckoutUpdateCartParam) {
            update_cart_occ(param: ${"$"}update) {
                error_message
                status
                data {
                    messages
                    success
                }
            }
        }
        """.trimIndent()
    }
}