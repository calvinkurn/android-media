package com.tokopedia.minicart.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.mapper.MiniCartSimplifiedMapper
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
    private val miniCartSimplifiedMapper: MiniCartSimplifiedMapper,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetMiniCartParam, MiniCartSimplifiedData>(dispatchers.io) {

    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetMiniCartParam): MiniCartSimplifiedData {
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
            return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(response.miniCart)
        } else {
            throw ResponseErrorException(response.miniCart.errorMessage.joinToString(", "))
        }
    }

    companion object {
        val QUERY = """
        query mini_cart_v3(${'$'}lang: String, ${'$'}additional_params: CartRevampAdditionalParams) {
          status
          mini_cart_v3(lang: ${'$'}lang, additional_params: ${'$'}additional_params) {
            error_message
            status
            data {
              errors
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
              available_section {
                available_group {
                  cart_string
                  shop {
                    shop_id
                    shop_name
                    shop_type_info {
                      title_fmt
                    }
                  }
                  shipment_information {                                        
                    free_shipping {
                      eligible
                    }
                    free_shipping_extra {
                      eligible
                    }
                  }
                  cart_details {
                    cart_detail_info {
                      cart_detail_type
                      bmgm {
                        offer_id
                        offer_name
                        offer_message
                        total_discount
                        offer_json_data
                        offer_status
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
                    }
                    products {
                      cart_id
                      parent_id
                      product_id
                      product_quantity
                      product_notes
                      campaign_id
                      product_tracker_data {
                        attribution
                      }
                      product_weight
                      slash_price_label                                                                   
                      warehouse_id                      
                      category_id
                      category
                      product_name
                      variant_description_detail {
                        variant_name
                      }
                      free_shipping_general {
                        bo_name
                        bo_type
                        badge_url
                      }
                      product_price
                      product_quantity
                      product_invenage_value
                      product_switch_invenage
                    }
                  }
                }
              }
              unavailable_section {
                unavailable_group {
                  shop {
                    shop_id
                    shop_name
                    shop_type_info {
                      title_fmt
                    }
                  }
                  shipment_information {
                    free_shipping {
                      eligible
                    }
                    free_shipping_extra {
                      eligible
                    }
                  }
                  cart_details {
                    cart_id
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
                    }
                    products {
                      cart_id
                      parent_id
                      product_id
                      product_quantity
                      product_notes                      
                      campaign_id
                      product_tracker_data {
                        attribution
                      }
                      product_weight
                      slash_price_label                                                                   
                      warehouse_id                      
                      category_id
                      category
                      product_name
                      variant_description_detail {
                        variant_name
                      }
                      product_price
                      product_quantity
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent()
    }
}
