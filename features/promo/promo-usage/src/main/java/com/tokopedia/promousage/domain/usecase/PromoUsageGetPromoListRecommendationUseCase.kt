package com.tokopedia.promousage.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.promousage.data.request.GetPromoListRecommendationParam
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@GqlQuery(
    PromoUsageGetPromoListRecommendationUseCase.QUERY_NAME,
    PromoUsageGetPromoListRecommendationUseCase.QUERY
)
class PromoUsageGetPromoListRecommendationUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<GetPromoListRecommendationParam, GetPromoListRecommendationResponse>(
    Dispatchers.IO
) {

    override suspend fun execute(params: GetPromoListRecommendationParam): GetPromoListRecommendationResponse {
        return repository.request(PromoUsageGetPromoListRecommendationQuery(), params)
    }

    override fun graphqlQuery(): String = QUERY.trimIndent()

    companion object {
        const val QUERY_NAME: String = "PromoUsageGetPromoListRecommendationQuery"
        const val QUERY: String = """
            mutation getPromoListRecommendation(${'$'}params: GetPromoListRecomendationRequest, ${'$'}chosen_address: PromoDisplayChosenAddressParam, ${'$'}is_promo_revamp: Boolean) {
              GetPromoListRecommendation(params: ${'$'}params, chosen_address: ${'$'}chosen_address, is_promo_revamp: ${'$'}is_promo_revamp) {
                data {
                  result_status {
                    success
                    message
                    code
                  }
                  promo_recommendation {
                    codes
                    message
                    message_selected_state
                    background_url
                    animation_url
                  }
                  coupon_sections {
                    id
                    title
                    is_enabled
                    is_collapsed
                    coupon_groups {
                      catalog_id
                      count
                    }
                    coupons {
                      code
                      benefit_type_str
                      benefit_amount_str
                      message
                      expiry_info
                      expiry_countdown
                      unique_id
                      shop_id
                      benefit_amount
                      is_recommended
                      is_selected
                      is_attempted
                      is_highlighted
                      radio_check_state
                      is_group_header
                      group_id
                      promo_id
                      is_bebas_ongkir
                      index
                      coupon_type
                      expiry_info
                      secondary_coupons {
                        code
                        benefit_type_str
                        benefit_amount_str
                        message
                        expiry_info
                        expiry_countdown
                        unique_id
                        shop_id
                        benefit_amount
                        is_recommended
                        is_selected
                        is_attempted
                        is_highlighted
                        radio_check_state
                        group_id
                        promo_id
                        is_bebas_ongkir
                        index
                        coupon_type
                        expiry_info
                      }
                      clashing_infos {
                        code
                        message
                        icon
                      }
                      cta {
                        text
                        app_link
                        type
                      }
                      promo_infos {
                        title
                        icon
                        info_type
                        validation_type
                        methods
                      }
                      benefit_details {
                        amount_idr
                        benefit_type
                        data_type
                      }
                      bo_clashing_infos {
                        code
                        message
                        icon
                      }
                      additional_bo_datas {
                        code
                        unique_id
                        shipping_id
                        sp_id
                        benefit_amount
                        shipping_price
                        shipping_subsidy
                        bo_campaign_id
                        benefit_class
                        eta_txt
                        cart_string_group
                        shipping_metadata {
                          unique_id
                          json_string
                        }
                      }
                      coupon_card_details {
                        state
                        color
                        icon_url
                        background_url
                      }
                    }
                  }
                  attempted_promo_code_error {
                    code
                    message
                  }
                  entry_point_info {
                    messages
                    state
                    icon_url
                    clickable
                  }
                  ticker_info {
                    message
                    icon_url
                    background_url
                  }
                  additional_message
                  user_group_metadata
                }
              }
            }
        """
    }
}
