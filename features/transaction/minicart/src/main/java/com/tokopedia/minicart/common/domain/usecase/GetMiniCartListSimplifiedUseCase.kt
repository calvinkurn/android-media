package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.minicart.bmgm.domain.model.BmgmParamModel
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase.Companion.PARAM_VALUE_MINICART
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

@Deprecated(
    "This usecase is no longer maintained since Feb 2023",
    ReplaceWith(
        "GetMiniCartWidgetUseCase",
        "com.tokopedia.minicart.domain.GetMiniCartWidgetUseCase"
    )
)
class GetMiniCartListSimplifiedUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
) : UseCase<MiniCartSimplifiedData>() {

    private var params: Map<String, Any>? = null
    private var shopIds: List<String> = emptyList()
    private var delay: Long = 0

    fun setParams(shopIds: List<String>, source: MiniCartSource, isShopDirectPurchase: Boolean = false, delay: Long = 0) {
        params = mapOf(
            GetMiniCartListUseCase.PARAM_KEY_LANG to GetMiniCartListUseCase.PARAM_VALUE_ID,
            GetMiniCartListUseCase.PARAM_KEY_ADDITIONAL to mapOf(
                GetMiniCartListUseCase.PARAM_KEY_SHOP_IDS to shopIds,
                ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress(),
                GetMiniCartListUseCase.PARAM_KEY_SOURCE to source.value,
                GetMiniCartListUseCase.PARAM_KEY_SHOP_DIRECT_PURCHASE to isShopDirectPurchase
            )
        )
        this.shopIds = shopIds
        this.delay = delay
    }

    fun setParams(shopIds: List<String>, source: MiniCartSource, bmGmParam: BmgmParamModel) {
        params = mapOf(
            GetMiniCartListUseCase.PARAM_KEY_LANG to GetMiniCartListUseCase.PARAM_VALUE_ID,
            GetMiniCartListUseCase.PARAM_KEY_ADDITIONAL to mapOf(
                GetMiniCartListUseCase.PARAM_KEY_SHOP_IDS to shopIds,
                ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress(),
                GetMiniCartListUseCase.PARAM_KEY_SOURCE to source.value,
                GetMiniCartListUseCase.PARAM_KEY_USE_CASE to PARAM_VALUE_MINICART,
                GetMiniCartListUseCase.PARAM_KEY_BMGM to bmGmParam
            )
        )
        this.shopIds = shopIds
        this.delay = delay
    }

    fun setParams(shopIds: List<String>, source: MiniCartSource, bmGmParam: BmgmParamModel, usecase: String) {
        params = mapOf(
            GetMiniCartListUseCase.PARAM_KEY_LANG to GetMiniCartListUseCase.PARAM_VALUE_ID,
            GetMiniCartListUseCase.PARAM_KEY_ADDITIONAL to mapOf(
                GetMiniCartListUseCase.PARAM_KEY_SHOP_IDS to shopIds,
                ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress(),
                GetMiniCartListUseCase.PARAM_KEY_SOURCE to source.value,
                GetMiniCartListUseCase.PARAM_KEY_USE_CASE to usecase,
                GetMiniCartListUseCase.PARAM_KEY_BMGM to bmGmParam
            )
        )
        this.shopIds = shopIds
        this.delay = delay
    }

    fun setParams(shopIds: List<String>, promoId: String, promoCode: String, source: MiniCartSource, delay: Long = 0) {
        params = mapOf(
            GetMiniCartListUseCase.PARAM_KEY_LANG to GetMiniCartListUseCase.PARAM_VALUE_ID,
            GetMiniCartListUseCase.PARAM_KEY_ADDITIONAL to mapOf(
                GetMiniCartListUseCase.PARAM_KEY_SHOP_IDS to shopIds,
                GetMiniCartListUseCase.PARAM_KEY_PROMO to mapOf(
                    GetMiniCartListUseCase.PARAM_KEY_PROMO_ID to promoId,
                    GetMiniCartListUseCase.PARAM_KEY_PROMO_CODE to promoCode
                ),
                ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress(),
                GetMiniCartListUseCase.PARAM_KEY_SOURCE to source.value
            )
        )
        this.delay = delay
    }

    override suspend fun executeOnBackground(): MiniCartSimplifiedData {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }
        if (delay > 0) {
            delay(delay)
        }

        val request = GraphqlRequest(QUERY, MiniCartGqlResponse::class.java, params)
        val response = graphqlRepository.response(listOf(request)).getSuccessData<MiniCartGqlResponse>()

        if (response.miniCart.status == "OK") {
            return response.miniCart.toSimplifiedData()
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
                          products_benefit {
                            product_cache_image_url
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
