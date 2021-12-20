package com.tokopedia.promocheckoutmarketplace.domain.usecase

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.data.request.CouponListRecommendationRequest
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetCouponListRecommendationUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository,
                                                             private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<CouponListRecommendationResponse>() {

    private var params: Map<String, Any?>? = null

    // Todo : remove context
    private var tmpContext: Context? = null

    fun setParams(promoRequest: PromoRequest, chosenAddress: ChosenAddress?, context: Context? = null) {
        tmpContext = context

        params = mapOf(
                KEY_PARAMS to CouponListRecommendationRequest(promoRequest = promoRequest),
                // Add current selected address from local cache
                ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to (chosenAddress
                        ?: chosenAddressRequestHelper.getChosenAddress())
        )
    }

    override suspend fun executeOnBackground(): CouponListRecommendationResponse {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val request = GraphqlRequest(QUERY, CouponListRecommendationResponse::class.java, params)
        return graphqlRepository.response(listOf(request)).getSuccessData()

        // Todo : remove dummy
//        val responseText = GraphqlHelper.loadRawString(tmpContext?.resources, R.raw.dummy_promo_list_response)
//        return Gson().fromJson(responseText, CouponListRecommendationResponse::class.java)
    }

    companion object {
        private const val KEY_PARAMS = "params"

        val QUERY = """
            mutation coupon_list_recommendation(${'$'}params: PromoStackRequest, ${'$'}chosen_address: ChosenAddressParam) {
                coupon_list_recommendation(params: ${'$'}params, chosen_address: ${'$'}chosen_address) {
                    message
                    error_code
                    status
                    data {
                        error_page {
                            is_show_error_page
                            image
                            title
                            description
                            button {
                                text
                                destination
                            }
                        }
                        result_status {
                            code
                            message
                            reason
                        }
                        empty_state {
                            title
                            description
                            image_url
                        }
                        title
                        sub_title
                        promo_recommendation {
                            codes
                            message
                        }
                        coupon_sections {
                            title
                            sub_title
                            icon_url
                            is_enabled
                            is_collapsed
                            tags
                            id
                            coupon_groups {
                                id
                                count
                            }
                            coupons {
                                code
                                title
                                message
                                expiry_info
                                expiry_count_down
                                coupon_url
                                coupon_app_link
                                unique_id
                                shop_id
                                benefit_amount
                                is_recommended
                                is_selected
                                is_attempted
                                radio_check_state
                            }
                            sub_sections {
                                title
                                sub_title
                                icon_url
                                is_enabled
                                is_collapsed
                                tags
                                id
                                coupon_groups {
                                    id
                                    count
                                }
                                coupons {
                                    code
                                    title
                                    message
                                    expiry_info
                                    expiry_count_down
                                    coupon_url
                                    coupon_app_link
                                    unique_id
                                    shop_id
                                    tag_image_urls
                                    benefit_amount
                                    is_recommended
                                    is_selected
                                    is_attempted
                                    radio_check_state
                                    clashing_infos {
                                        code
                                        message
                                        icon
                                    }
                                    currency_details_str
                                    coachmark {
                                        is_shown
                                        title
                                        content
                                    }
                                    is_highlighted
                                    group_id
                                    is_group_header
                                    promo_infos {
                                         type
                                         title
                                         icon
                                    }
                                }
                            }
                        }
                        attempted_promo_code_error {
                            code
                            message
                        }
                        additional_message
                        reward_points_info {
                            message
                            gain_reward_points_tnc {
                                title
                                tnc_details {
                                    icon_image_url
                                    description
                                }
                            }
                        }
                        section_tabs {
                            id
                            title
                        }
                    }
                }
            }
        """.trimIndent()
    }

}