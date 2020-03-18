package com.tokopedia.purchase_platform.features.one_click_checkout.order.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout.CheckoutOccGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout.CheckoutOccRequest
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout.PromoRequest
import javax.inject.Inject

class CheckoutOccUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<CheckoutOccGqlResponse>) {

    fun execute(param: CheckoutOccRequest, onSuccess: (CheckoutOccGqlResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setTypeClass(CheckoutOccGqlResponse::class.java)
        graphqlUseCase.setRequestParams(generateParam(param))

        graphqlUseCase.execute({ checkoutOccGqlResponse: CheckoutOccGqlResponse ->
            onSuccess(checkoutOccGqlResponse)
        }, { throwable: Throwable ->
            onError(throwable)
        })
    }

    private fun generateParam(param: CheckoutOccRequest): Map<String, Any?> {
        return mapOf(
                "params" to mapOf(
                        "profile" to mapOf(
                                "profile_id" to param.profile.profileId
                        ),
                        "carts" to mapOf(
                                "promos" to generatePromoListParams(param.carts.promos),
                                "data" to listOf(
                                        mapOf(
                                                "address_id" to param.carts.data[0].addressId,
                                                "shop_products" to listOf(
                                                        mapOf(
                                                                "promos" to generatePromoListParams(param.carts.data[0].shopProducts[0].promos),
                                                                "shop_id" to param.carts.data[0].shopProducts[0].shopId,
                                                                "product_data" to listOf(
                                                                        mapOf(
                                                                                "product_id" to param.carts.data[0].shopProducts[0].productData[0].productId,
                                                                                "product_quantity" to param.carts.data[0].shopProducts[0].productData[0].productQuantity,
                                                                                "product_notes" to param.carts.data[0].shopProducts[0].productData[0].productNotes
                                                                        )
                                                                ),
                                                                "warehouse_id" to param.carts.data[0].shopProducts[0].warehouseId,
                                                                "is_preorder" to param.carts.data[0].shopProducts[0].isPreorder,
                                                                "finsurance" to param.carts.data[0].shopProducts[0].finsurance,
                                                                "shipping_info" to mapOf(
                                                                        "shipping_id" to param.carts.data[0].shopProducts[0].shippingInfo.shippingId,
                                                                        "sp_id" to param.carts.data[0].shopProducts[0].shippingInfo.spId,
                                                                        "rates_id" to param.carts.data[0].shopProducts[0].shippingInfo.ratesId,
                                                                        "ut" to param.carts.data[0].shopProducts[0].shippingInfo.ut,
                                                                        "checksum" to param.carts.data[0].shopProducts[0].shippingInfo.checksum
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        )
    }

    private fun generatePromoListParams(param: List<PromoRequest>): List<Map<String, Any?>> {
        val list: ArrayList<Map<String, Any?>> = ArrayList()
        for (promoRequest in param) {
            list.add(mapOf(
                    "type" to promoRequest.type,
                    "code" to promoRequest.code
            ))
        }
        return list
    }

    companion object {
        val QUERY = """
            mutation one_click_checkout(${"$"}params: oneClickCheckoutParams) {
              one_click_checkout(params:${"$"}params) {
                header {
                  process_time
                  reason
                  error_code
                  messages
                }
                data {
                  success
                  error {
                    code
                    image_url
                    message
                    additional_info {
                        price_validation {
                            is_updated
                            message {
                                title
                                desc
                                action
                            }
                            tracker_data {
                                product_changes_type
                                campaign_type
                                product_ids
                            }
                        }
                    }
                  }
                  payment_parameter {
                    callback_url
                    payload
                    redirect_param {
                      url
                      gateway
                      method
                      form
                    }
                  }
                }
                status
              }
            }
        """.trimIndent()


    }
}