package com.tokopedia.promousage.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.promousage.data.request.ValidateUsePromoUsageParam
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@GqlQuery(
    PromoUsageValidateUseUseCase.QUERY_NAME,
    PromoUsageValidateUseUseCase.QUERY
)
class PromoUsageValidateUseUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository
) : CoroutineUseCase<ValidateUsePromoUsageParam, ValidateUseResponse>(Dispatchers.IO) {

    override suspend fun execute(params: ValidateUsePromoUsageParam): ValidateUseResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = QUERY.trimIndent()

    companion object {
        const val QUERY_NAME = "PromoUsageValidateUseQuery"
        const val QUERY: String = """
            mutation validateUsePromoRevamp(${"$"}params: PromoStackRequest, ${"$"}chosen_address: ChosenAddressParam) {
              validate_use_promo_revamp(params: ${"$"}params, chosen_address: ${"$"}chosen_address) {
                status
                message
                 error_code
                 code
                 promo {
                   global_success
                   message {
                     state
                     color
                     text
                   }
                   success
                   codes
                   promo_code_id
                   title_description
                   discount_amount
                   cashback_wallet_amount
                   cashback_advocate_referral_amount
                   cashback_voucher_description
                   invoice_description
                   gateway_id
                   is_coupon
                   coupon_description
                   is_tokopedia_gerai
                   voucher_orders {
                     code
                     success
                     unique_id
                     cart_string_group
                     shipping_id
                     sp_id
                     cart_id
                     order_id
                     shop_id
                     is_po
                     duration
                     warehouse_id
                     address_id
                     type
                     cashback_wallet_amount
                     discount_amount
                     invoice_description
                     title_description
                     message {
                       state
                       color
                       text
                     }
                     benefit_details{
                       code
                       type
                       order_id
                       unique_id
                       cart_string_group
                       discount_amount
                       discount_details{
                         amount
                         data_type
                       }
                       cashback_amount
                       cashback_details{
                         amount_idr
                         amount_points
                         benefit_type
                       }
                       promo_type{
                         is_bebas_ongkir
                         is_exclusive_shipping
                       }
                       benefit_product_details{
                         product_id
                         cashback_amount
                         cashback_amount_idr
                         discount_amount
                         is_bebas_ongkir
                       }
                     }
                   }
                   benefit_details{
                       code
                       type
                       order_id
                       unique_id
                       cart_string_group
                       discount_amount
                       discount_details{
                         amount
                         data_type
                       }
                       cashback_amount
                       cashback_details{
                         amount_idr
                         amount_points
                         benefit_type
                       }
                       promo_type{
                         is_bebas_ongkir
                         is_exclusive_shipping
                       }
                       benefit_product_details{
                         product_id
                         cashback_amount
                         cashback_amount_idr
                         discount_amount
                         is_bebas_ongkir
                       }
                     }
                   benefit_summary_info {
                     final_benefit_text
                     final_benefit_amount
                     final_benefit_amount_str
                     summaries {
                       description
                       type
                       amount_str
                       amount
                       section_name
                       section_description
                       details{
                         section_name
                         description
                         type
                         amount
                         amount_str
                         points
                         points_str
                       }
                     }
                   }
                   clashing_info_detail {
                     clash_message
                     clash_reason
                     is_clashed_promos
                     options {
                       voucher_orders {
                         cart_id
                         code
                         shop_name
                         potential_benefit
                         promo_name
                         unique_id
                       }
                     }
                   }
                   tracking_details{
                     product_id
                     promo_codes_tracking
                     promo_details_tracking
                   }
                   tokopoints_detail{
                     conversion_rate{
                       rate
                       points_coefficient
                       external_currency_coefficient
                     }
                   }
                   ticker_info{
                     unique_id
                     status_code
                     message
                   }
                   additional_info{
                     message_info{
                       message
                       detail
                     }
                     error_detail{
                       message
                     }
                     empty_cart_info {
                       image_url
                       message
                       detail
                     }
                     usage_summaries {
                       description
                       type
                       amount_str
                       amount
                       currency_details_str
                     }
                     sp_ids
                     promo_sp_ids {
                       unique_id
                       mvc_shipping_benefits {
                         benefit_amount
                         sp_id
                       }
                     }
                     poml_auto_applied
                   }
                 }
              }
            }
        """
    }
}
