package com.tokopedia.purchase_platform.features.promo.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.features.promo.data.response.CouponListRecommendationResponse
import com.tokopedia.purchase_platform.features.promo.presentation.MOCK_RESPONSE_PHONE_NOT_VERIF
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetCouponListRecommendationUseCase @Inject constructor(private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<CouponListRecommendationResponse>() {

    var params = HashMap<String, Any>()

    companion object {
        val MUTATION = """
        mutation coupon_list_recommendation(${'$'}params: PromoStackRequest) {
            coupon_list_recommendation(params: ${'$'}params, dummy: 200) {
                message
                error_code
                status
                data {
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
                                }
                            }
                        }
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
                }
            }
        }
        """.trimIndent()

        lateinit var promoRequest: PromoRequest

        fun generateCouponListRequest() {
            promoRequest = PromoRequest().apply {

            }
        }

        fun getParam(): Map<String, Any> {
            val param = HashMap<String, Any>()
            param["promo"] = promoRequest

            return param
        }
    }

    override suspend fun executeOnBackground(): CouponListRecommendationResponse {
        val couponListRecommendation = Gson().fromJson(MOCK_RESPONSE_PHONE_NOT_VERIF, CouponListRecommendationResponse::class.java)
//        val gqlRequest = GraphqlRequest(MUTATION, GqlCouponListRecommendationResponse::class.java, getParam())
//        gqlUseCase.clearRequest()
//        gqlUseCase.addRequest(gqlRequest)
//        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
//                .Builder(CacheType.ALWAYS_CLOUD).build())
//
//        val gqlResponse = gqlUseCase.executeOnBackground()
//        val couponListRecommendation = gqlResponse.getData<GqlCouponListRecommendationResponse>(GqlCouponListRecommendationResponse::class.java)

        return couponListRecommendation
    }

}