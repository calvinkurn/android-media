package com.tokopedia.minicart.bmgm.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongSafely
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.minicart.bmgm.domain.mapper.BmgmMiniCartDataMapper
import com.tokopedia.minicart.bmgm.domain.model.BmgmParamModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 03/08/23.
 */

@GqlQuery("GetBmgmMiniCartDataQuery", GetBmgmMiniCartDataUseCase.QUERY)
class GetBmgmMiniCartDataUseCase @Inject constructor(
    private val mapper: BmgmMiniCartDataMapper,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<MiniCartData>(graphqlRepository) {

    init {
        setGraphqlQuery(GetBmgmMiniCartDataQuery())
        setTypeClass(MiniCartData::class.java)
    }

    suspend operator fun invoke(shopId: String, bmgmParam: BmgmParamModel): BmgmMiniCartDataUiModel {
        try {
            val requestParam = createRequestParam(shopId, bmgmParam)
            setRequestParams(requestParam.parameters)
            val response = executeOnBackground()
            return mapper.mapToUiModel(response)
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

    private fun createRequestParam(shopId: String, bmgmParam: BmgmParamModel): RequestParams {
        return RequestParams.create().apply {
            putString(PARAM_KEY_LANG, PARAM_VALUE_ID)
            putObject(
                PARAM_KEY_ADDITIONAL, mapOf(
                    PARAM_KEY_SHOP_IDS to listOf(shopId.toLongSafely()),
                    KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress(),
                    PARAM_KEY_SOURCE to PARAM_VALUE_SOURCE,
                    PARAM_KEY_BMGM to bmgmParam
                )
            )
        }
    }

    companion object {
        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_KEY_ADDITIONAL = "additional_params"
        private const val PARAM_KEY_SHOP_IDS = "shop_ids"
        private const val PARAM_KEY_SOURCE = "source"
        private const val PARAM_KEY_BMGM = "bmgm"

        private const val PARAM_VALUE_ID = "id"
        private const val PARAM_VALUE_SOURCE = "bmgm_olp_mini_cart"

        const val QUERY = """
            query mini_cart_v3(${'$'}lang: String, $${'$'}additional_params: CartRevampAdditionalParams) {
              mini_cart_v3(lang: $${'$'}lang, additional_params: $${'$'}additional_params) {
                error_message
                status
                data {
                  shopping_summary {
                    total_original_value
                    total_value
                  }
                  available_section {
                    available_group {
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
                        products {
                          cart_id
                          product_id
                          product_quantity
                          product_name
                          product_image {
                            image_src_100_square
                          }
                          warehouse_id
                        }
                      }
                    }
                  }
                }
              }
            }
        """
    }
}