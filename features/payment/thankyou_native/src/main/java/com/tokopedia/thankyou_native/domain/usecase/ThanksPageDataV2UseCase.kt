package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.GQL_THANK_YOU_PAGE_DATA
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.model.ThanksPageResponse
import javax.inject.Inject
import javax.inject.Named

class ThanksPageDataV2UseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ThanksPageResponse>(graphqlRepository)  {

    fun getThankPageData(onSuccess: (ThanksPageData) -> Unit,
                         onError: (Throwable) -> Unit, paymentId: String, merchant: String) {
        try {
            this.setTypeClass(ThanksPageResponse::class.java)
            this.setRequestParams(getRequestParams(paymentId, merchant))
            this.setGraphqlQuery(QUERY)
            this.execute(
                { result ->
                    onSuccess(result.thanksPageData)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(paymentId: String, merchant: String): Map<String, Any> {
        return mapOf(
            PARAM_PAYMENT_ID to paymentId,
            PARAM_MERCHANT to merchant,
            PARAM_LANG to "id"
        )
    }

    companion object {
        private const val PARAM_PAYMENT_ID = "paymentIDStr"
        private const val PARAM_MERCHANT = "merchant"
        private const val PARAM_LANG = "lang"
        private const val QUERY = """
            query ThanksPageDataV2(${'$'}paymentIDStr : String!, ${'$'}merchant: String!, ${'$'}lang: String!){
              thanksPageDataV2(paymentIDStr: ${'$'}paymentIDStr, merchant: ${'$'}merchant, lang: ${'$'}lang){
                current_site
                business_unit   
                merchant_code
                profile_code
                payment_id
                payment_status
                payment_type
                gateway_name
                gateway_image
                expire_time_unix
                expire_time_str
                page_type
                title
                message
                order_amount
                order_amount_str
                amount
                amount_str
                combine_amount
                payment_items{
                    item_name
                    item_desc
                    amount
                    amount_str
                }
                payment_deduction {
                    item_name
                    item_desc
                    amount
                    amount_str
                }
                payment_details {
                   gateway_code
                   gateway_name
                   amount
                   amount_str
                   amount_combine
                   amount_combine_str
                }
                promo_data {
                    promo_code
                    promo_desc
                    total_cashback
                    total_cashback_str
                    total_discount
                    total_discount_str
                }
                order_group_list{
                    id
                    total_shipping_fee
                    total_bebasongkir_price
                    dest_address
                    shipping_service_name
                    total_insurance_price
                    shipper_name
                    shipper_eta
                }
                order_list{
                    order_id
                    store_id
                    order_group_id
                    store_type
                    logistic_type
                    store_name
                    add_ons_section_description
                    addon_item {
                        name
                        quantity
                        price_str
                    }
                    item_list{
                        unique_id
                        parent_product_id
                        product_id
                        product_name
                        product_brand
                        price
                        price_str
                        quantity
                        product_plan_protection
                        weight
                        weight_unit
                        total_price
                        total_price_str
                        promo_code
                        category
                        variant
                        thumbnail_product
                        bebas_ongkir_dimension
                        is_bbi
                        category_id
                        bundle_group_id
                        addon_item {
                            name
                            quantity
                            price_str
                        }
                    }
                    bundle_group_data {
                      group_id
                      icon
                      title
                      total_price
                      total_price_str
                    }
                    shipping_amount
                    shipping_amount_str
                    total_discount_shipping
                    total_discount_product
                    shipping_desc
                    insurance_amount
                    insurance_amount_str
                    logistic_duration
                    logistic_eta
                    address
                    promo_data{
                        promo_code
                        promo_desc
                        total_cashback
                        total_cashback_str
                        total_discount
                        total_discount_str
                    }
                    tax
                    coupon
                    revenue
                }
                additional_info{
                    account_number
                    account_dest
                    bank_name
                    bank_branch
                    payment_code
                    masked_number
                    installment_info
                    interest
                    revenue
                }
                how_to_pay
                how_to_pay_applink
                event
                event_category
                event_action
                event_label
                currency_code
                push_gtm
                new_user
                is_mub
                custom_data_applink
                custom_data_message
                custom_data_other
                config_flag
                config_list
                gateway_additional_data{
                   key
                   value
                }
                fee_details{
                    name
                    amount
                    show_tooltip
                    tooltip_title
                    tooltip_desc
                }
                thanks_summaries {
                    key
                    description
                    message
                    is_cta
                    cta_text
                    cta_link
                }
                cta_data_thanks_page {
                    primary {
                        type
                        text
                        url
                        applink
                        hide_button
                    }
                    secondary {
                        type
                        text
                        url
                        applink
                        hide_button
                    }
                }
              }
            }
        """
    }

}
