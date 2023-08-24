package com.tokopedia.promocheckoutmarketplace.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promocheckoutmarketplace.data.request.CouponListRecommendationRequest
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

@GqlQuery(GetCouponListRecommendationUseCase.QUERY_NAME, GetCouponListRecommendationUseCase.QUERY)
class GetCouponListRecommendationUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
) : UseCase<CouponListRecommendationResponse>() {

    private var params: Map<String, Any?>? = null

    fun setParams(promoRequest: PromoRequest, chosenAddress: ChosenAddress?) {
        params = mapOf(
            KEY_PARAMS to CouponListRecommendationRequest(promoRequest = promoRequest),
            // Add current selected address from local cache
            ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to (
                chosenAddress
                    ?: chosenAddressRequestHelper.getChosenAddress()
                )
        )
    }

    override suspend fun executeOnBackground(): CouponListRecommendationResponse {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val request = GraphqlRequest(CouponListRecommendationQuery.GQL_QUERY, CouponListRecommendationResponse::class.java, params)
        return graphqlRepository.response(listOf(request)).getSuccessData()
    }

    companion object {
        private const val KEY_PARAMS = "params"

        const val QUERY_NAME = "CouponListRecommendationQuery"
        const val QUERY = """
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
                            is_enabled
                            id
                            sub_sections {
                                title
                                sub_title
                                icon_unify
                                icon_url
                                is_enabled
                                id
                                coupon_groups {
                                    id
                                    count
                                }
                                coupons {
                                    promo_id
                                    code
                                    title
                                    message
                                    coupon_app_link
                                    unique_id
                                    shop_id
                                    benefit_amount
                                    is_recommended
                                    is_selected
                                    is_attempted
                                    is_bebas_ongkir
                                    clashing_infos {
                                        code
                                        message
                                        icon
                                    }
                                    bo_clashing_infos {
                                        code
                                        message
                                        icon
                                    }
                                    additional_bo_datas {
                                        code
                                        unique_id
                                        cart_string_group
                                        shipping_id
                                        sp_id
                                        benefit_amount
                                        promo_id
                                        shipping_subsidy
                                        shipping_price
                                        benefit_class
                                        bo_campaign_id
                                        eta_txt
                                    }
                                    currency_details_str
                                    cta {
                                        text
                                        url
                                        app_link
                                        type
                                        json_metadata
                                    }
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
                                         validation_type
                                         methods
                                    }
                                    benefit_details {
                                        amount_idr
                                        benefit_type
                                        data_type
                                    }
                                    benefit_adjustment_message
                                    secondary_coupons {
                                        promo_id
                                        code
                                        title
                                        message
                                        coupon_app_link
                                        unique_id
                                        shop_id
                                        benefit_amount
                                        is_recommended
                                        is_selected
                                        is_attempted
                                        is_bebas_ongkir
                                        clashing_infos {
                                            code
                                            message
                                            icon
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
                                            promo_id
                                            shipping_subsidy
                                            shipping_price
                                            benefit_class
                                            bo_campaign_id
                                            eta_txt
                                        }
                                        currency_details_str
                                        is_highlighted
                                        promo_infos {
                                             type
                                             title
                                             icon
                                             validation_type
                                             methods
                                        }
                                        benefit_details {
                                            amount_idr
                                            benefit_type
                                            data_type
                                        }
                                    }
                                }
                            }
                        }
                        attempted_promo_code_error {
                            code
                            message
                        }
                        section_tabs {
                            id
                            title
                        }
                        bottom_sheet {
                            title
                            content_title
                            content_description
                            image_url
                            button_txt
                        }
                    }
                }
            }
        """
    }
}
