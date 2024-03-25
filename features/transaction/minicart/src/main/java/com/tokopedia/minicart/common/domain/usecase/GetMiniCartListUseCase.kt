package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

@Deprecated(
    "This usecase is no longer maintained since Feb 2023",
    ReplaceWith(
        "GetMiniCartWidgetUseCase",
        "com.tokopedia.minicart.domain.GetMiniCartWidgetUseCase"
    )
)
class GetMiniCartListUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
) : UseCase<MiniCartData>() {

    private var params: Map<String, Any>? = null

    fun setParams(
        shopIds: List<String>,
        isShopDirectPurchase: Boolean = false,
        source: MiniCartSource = MiniCartSource.MiniCartBottomSheet
    ) {
        params = mapOf(
            PARAM_KEY_LANG to PARAM_VALUE_ID,
            PARAM_KEY_ADDITIONAL to mapOf(
                PARAM_KEY_SHOP_IDS to shopIds,
                KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress(),
                PARAM_KEY_SOURCE to source.value,
                PARAM_KEY_SHOP_DIRECT_PURCHASE to isShopDirectPurchase
            )
        )
    }

    override suspend fun executeOnBackground(): MiniCartData {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val request = GraphqlRequest(QUERY, MiniCartGqlResponse::class.java, params)
        val response = graphqlRepository.response(listOf(request)).getSuccessData<MiniCartGqlResponse>()

        if (response.miniCart.status == "OK") {
            return response.miniCart
        } else {
            throw ResponseErrorException(response.miniCart.errorMessage.joinToString(", "))
        }
    }

    companion object {
        const val PARAM_KEY_LANG = "lang"
        const val PARAM_KEY_SELECTED_CART_ID = "selected_cart_id"
        const val PARAM_KEY_ADDITIONAL = "additional_params"
        const val PARAM_KEY_SHOP_IDS = "shop_ids"
        const val PARAM_KEY_PROMO = "promo"
        const val PARAM_KEY_PROMO_ID = "promo_id"
        const val PARAM_KEY_PROMO_CODE = "promo_code"
        const val PARAM_KEY_SOURCE = "source"
        const val PARAM_KEY_BMGM = "bmgm"
        const val PARAM_KEY_SHOP_DIRECT_PURCHASE = "is_shop_direct_purchase"
        const val PARAM_KEY_USE_CASE = "usecase"
        const val PARAM_VALUE_ID = "id"
        const val PARAM_VALUE_MINICART = "minicart"

        val QUERY = """
            query mini_cart_v3(${'$'}lang: String, ${'$'}additional_params: CartRevampAdditionalParams) {
              status
              mini_cart_v3(lang: ${'$'}lang, additional_params: ${'$'}additional_params) {
                error_message
                status
                data {
                  errors
                  out_of_service {
                    id
                    code
                    image
                    title
                    description
                    buttons {
                      id
                      code
                      message
                      color
                    }
                  }
                  max_char_note
                  placeholder_note
                  header_title
                  beli_button_config {
                    button_type
                    button_wording
                  }
                  shopping_summary {
                    total_wording
                    total_value
                    discount_total_wording
                    discount_value
                    seller_cashback_wording
                    seller_cashback_value
                    payment_total_wording
                    payment_total_value
                  }
                  available_section {
                    action {
                      id
                      code
                      message
                    }
                    available_group {
                      cart_string
                      errors
                      shop {
                        maximum_weight_wording
                        maximum_shipping_weight
                        shop_id
                        shop_name
                        shop_type_info {
                          badge
                          shop_grade
                          shop_tier
                          title
                          title_fmt
                        }
                      }
                      shipment_information {
                        shop_location
                        estimation
                        free_shipping {
                          eligible
                          badge_url
                        }
                        free_shipping_extra {
                          eligible
                          badge_url
                        }
                      }
                      cart_details {
                        cart_detail_info {
                          cart_detail_type
                          bmgm {
                            offer_id
                            offer_icon
                            offer_message
                            offer_landing_page_link
                            tier_product {
                              tier_id
                              benefit_wording
                              action_wording
                              products_benefit {
                                product_id
                                product_name
                                product_cache_image_url
                                quantity
                              }
                            }
                          }
                        }
                        errors
                        bundle_detail { 
                          bundle_id
                          bundle_name
                          bundle_type
                          bundle_status
                          bundle_description
                          bundle_price
                          bundle_price_fmt
                          bundle_original_price
                          bundle_original_price_fmt
                          bundle_min_order
                          bundle_max_order
                          bundle_quota
                          edit_app_link
                          bundle_qty
                          bundle_group_id
                          slash_price_label
                          bundle_grayscale_icon_url
                          bundle_icon_url
                        }
                        products {
                          cart_id
                          product_id
                          product_weight
                          product_quantity
                          product_name
                          product_image {
                            image_src_100_square
                          }
                          variant_description_detail {
                            variant_name
                            variant_description
                          }
                          product_warning_message
                          slash_price_label
                          product_original_price
                          initial_price
                          product_price
                          product_information
                          product_tag_info {
                            message
                            icon_url
                          }
                          product_notes
                          product_min_order
                          product_max_order
                          product_invenage_value
                          product_switch_invenage
                          parent_id
                          wholesale_price {
                            qty_min
                            qty_max
                            prd_prc
                          }
                          campaign_id
                          product_tracker_data {
                            attribution
                          }
                          warehouse_id
                          category_id
                          category
                          product_cashback
                          selected_unavailable_action_link
                        }
                      }
                    }
                  }
                  unavailable_ticker
                  unavailable_section_action {
                    id
                    code
                    message
                  }
                  unavailable_section {
                    title
                    unavailable_description
                    selected_unavailable_action_id
                    action {
                      id
                      code
                      message
                    }
                    unavailable_group {
                      shipment_information {
                        shop_location
                        estimation
                        free_shipping {
                          eligible
                          badge_url
                        }
                        free_shipping_extra {
                          eligible
                          badge_url
                        }
                      }
                      shop {
                        maximum_weight_wording
                        maximum_shipping_weight
                        shop_id
                        shop_name
                        shop_type_info {
                          badge
                          shop_grade
                          shop_tier
                          title
                          title_fmt
                        }
                      }
                      cart_string
                      errors
                      cart_details {                    
                        errors
                        bundle_detail { 
                          bundle_id
                          bundle_name
                          bundle_type
                          bundle_status
                          bundle_description
                          bundle_price
                          bundle_price_fmt
                          bundle_original_price
                          bundle_original_price_fmt
                          bundle_min_order
                          bundle_max_order
                          bundle_quota
                          edit_app_link
                          bundle_qty
                          bundle_group_id
                          slash_price_label
                          bundle_grayscale_icon_url
                          bundle_icon_url
                        }
                        products {
                          cart_id
                          product_id
                          product_weight
                          product_quantity
                          product_name
                          product_image {
                            image_src_100_square
                          }
                          variant_description_detail {
                            variant_name
                            variant_description
                          }
                          product_warning_message
                          slash_price_label
                          product_original_price
                          initial_price
                          product_price
                          product_information
                          product_notes
                          product_min_order
                          product_max_order
                          parent_id
                          wholesale_price {
                            qty_min
                            qty_max
                            prd_prc
                          }
                          campaign_id
                          product_tracker_data {
                            attribution
                          }
                          warehouse_id
                          category_id
                          category
                          selected_unavailable_action_link
                        }
                      }
                    }
                  }
                  total_product_count
                  total_product_price
                  total_product_error
                }
              }
            }
        """.trimIndent()
    }
}
