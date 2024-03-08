package com.tokopedia.minicart.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSourceValue
import com.tokopedia.network.exception.ResponseErrorException
import kotlinx.coroutines.delay
import javax.inject.Inject

data class GetMiniCartParam(
    @SerializedName("shop_ids")
    val shopIds: List<String>,
    @SerializedName("source")
    val source: MiniCartSourceValue,
    @SerializedName("usecase")
    val useCase: String = "minicart",
    @SerializedName("is_shop_direct_purchase")
    val isShopDirectPurchase: Boolean = false,
    @SerializedName("promo")
    val promo: GetMiniCartPromoParam? = null,
    @SerializedName("bmgm")
    val bmgm: GetMiniCartBmgmParam? = null,
    @SerializedName("chosen_address")
    internal var chosenAddress: ChosenAddress = ChosenAddress(),
    @Transient
    val delay: Long = 0
) {

    data class GetMiniCartPromoParam(
        @SerializedName("promo_id")
        val promoId: Long = 0,
        @SerializedName("promo_code")
        val promoCode: String = ""
    )

    data class GetMiniCartBmgmParam(
        @SerializedName("offer_ids")
        val offerIds: List<Long> = emptyList(),
        @SerializedName("offer_json_data")
        val offerJsonData: String = "{}",
        @SerializedName("warehouse_ids")
        val warehouseIds: List<Long> = emptyList()
    )
}

class GetMiniCartWidgetUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetMiniCartParam, MiniCartData>(dispatchers.io) {

    @GqlQuery(MINI_CART_QUERY, QUERY)
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetMiniCartParam): MiniCartData {
        if (params.delay > 0) {
            delay(params.delay)
        }

        params.chosenAddress = chosenAddressRequestHelper.getChosenAddress()

        val response = graphqlRepository.request<Map<String, Any>, MiniCartGqlResponse>(
            graphqlQuery(),
            mapOf(
                GetMiniCartListUseCase.PARAM_KEY_LANG to GetMiniCartListUseCase.PARAM_VALUE_ID,
                GetMiniCartListUseCase.PARAM_KEY_ADDITIONAL to params
            )
        )

        if (response.miniCart.status == "OK") {
            return response.miniCart
        } else {
            throw ResponseErrorException(response.miniCart.errorMessage.joinToString(", "))
        }
    }

    companion object {
        private const val MINI_CART_QUERY = "MiniCartQuery"

        private const val QUERY = """
        query miniCartV3(${'$'}lang: String, ${'$'}additional_params: CartRevampAdditionalParams) {
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
              bottom_bar {
                text
                is_shop_active
                total_price_fmt
              }
              total_product_count
              total_product_error
              total_product_price
              simplified_shopping_summary {
                text
                sections {
                  title
                  description
                  icon_url
                  details {
                    name
                    value
                  }
                }
              }
              shopping_summary {
                total_wording
                total_original_value
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
                    errors
                    cart_detail_info {
                      cart_detail_type
                      bmgm {
                        offer_id
                        offer_type_id
                        offer_name
                        offer_message
                        total_discount
                        offer_json_data
                        offer_status
                        is_tier_achieved
                        tier_product {
                          tier_id
                          tier_message
                          tier_discount_text
                          tier_discount_amount
                          price_before_benefit
                          price_after_benefit
                          list_product {
                            product_id
                            warehouse_id
                            quantity
                            price_before_benefit
                            price_after_benefit
                            cart_id
                          }
                          benefit_wording
                          action_wording
                          benefit_quantity
                          products_benefit {
                            product_id
                            quantity
                            product_name
                            product_cache_image_url
                            original_price
                            final_price
                          }
                        }
                      }
                    }
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
                      parent_id
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
                      free_shipping_general {
                        bo_name
                        bo_type
                        badge_url
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
                      parent_id
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
                      free_shipping_general {
                        bo_name
                        bo_type
                        badge_url
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
        """
    }
}
