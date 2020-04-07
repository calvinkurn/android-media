package com.tokopedia.purchase_platform.features.one_click_checkout.order.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout.*
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
                PARAMS to mapOf(
                        CheckoutOccRequest.PARAM_PROFILE to mapOf(
                                Profile.PARAM_PROFILE_ID to param.profile.profileId
                        ),
                        CheckoutOccRequest.PARAM_CARTS to mapOf(
                                ParamCart.PARAM_PROMOS to generatePromoListParams(param.carts.promos),
                                ParamCart.PARAM_DATA to listOf(
                                        mapOf(
                                                ParamData.PARAM_ADDRESS_ID to param.carts.data[0].addressId,
                                                ParamData.PARAM_SHOP_PRODUCTS to listOf(
                                                        mapOf(
                                                                ShopProduct.PARAM_PROMOS to generatePromoListParams(param.carts.data[0].shopProducts[0].promos),
                                                                ShopProduct.PARAM_SHOP_ID to param.carts.data[0].shopProducts[0].shopId,
                                                                ShopProduct.PARAM_PRODUCT_DATA to listOf(
                                                                        mapOf(
                                                                                ProductData.PARAM_PRODUCT_ID to param.carts.data[0].shopProducts[0].productData[0].productId,
                                                                                ProductData.PARAM_PRODUCT_QUANTITY to param.carts.data[0].shopProducts[0].productData[0].productQuantity,
                                                                                ProductData.PARAM_PRODUCT_NOTES to param.carts.data[0].shopProducts[0].productData[0].productNotes
                                                                        )
                                                                ),
                                                                ShopProduct.PARAM_WAREHOUSE_ID to param.carts.data[0].shopProducts[0].warehouseId,
                                                                ShopProduct.PARAM_IS_PREORDER to param.carts.data[0].shopProducts[0].isPreorder,
                                                                ShopProduct.PARAM_FINSURANCE to param.carts.data[0].shopProducts[0].finsurance,
                                                                ShopProduct.PARAM_SHIPPING_INFO to mapOf(
                                                                        ShippingInfo.PARAM_SHIPPING_ID to param.carts.data[0].shopProducts[0].shippingInfo.shippingId,
                                                                        ShippingInfo.PARAM_SP_ID to param.carts.data[0].shopProducts[0].shippingInfo.spId,
                                                                        ShippingInfo.PARAM_RATES_ID to param.carts.data[0].shopProducts[0].shippingInfo.ratesId,
                                                                        ShippingInfo.PARAM_UT to param.carts.data[0].shopProducts[0].shippingInfo.ut,
                                                                        ShippingInfo.PARAM_CHECKSUM to param.carts.data[0].shopProducts[0].shippingInfo.checksum
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
                    PromoRequest.PARAM_TYPE to promoRequest.type,
                    PromoRequest.PARAM_CODE to promoRequest.code
            ))
        }
        return list
    }

    companion object {
        const val PARAMS = "params"

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