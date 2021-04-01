package com.tokopedia.promocheckout.common.util

object PromoQuery {
    fun promoDealsQuery() = """
      query travelCollectiveBanner {
        travelCollectiveBanner(product: DEALS, countryID: "ID") {
           banners{
              id
              product
              attributes{
                description
                webURL
                appURL
                imageURL
                promoCode
              }
             }
              meta{
                title
                webURL
                appURL
             }
           }
        }"""

    fun promoCheckoutDigitalCheckVoucher() = """
        query rechargeCheckVoucher(${'$'}data: RechargeInputVoucher!) {
          status
              rechargeCheckVoucher(voucher: ${'$'}data) {
                  data {
                    success
                    code
                    discount_amount
                    cashback_amount
                    promo_code_id
                    is_coupon
                    message {
                       state
                       color
                       text
                    }
                    title_description
                   }
                  errors{
                    status
                    title
                    }
                   } 
               }
            """

    fun promoCheckoutHotelCheckVoucher() = """
     query PropertyVoucher(${'$'}data: PropertyCheckVoucherRequest!) {
        propertyCheckVoucher(input: ${'$'}data) {
            code
            discount
            discountAmount
            cashback
            cashbackAmount
            message
            titleDescription
            isCoupon
            isSuccess
            errorMessage
        }
    }"""

    fun promoCheckoutUmrahCheckVoucher() = """
    query umrahPromoCheck(${'$'}params:UmrahPromoCheckInput!){
        umrahPromoCheck(params:${'$'}params){
            data{
                success
                message{
                    state
                    color
                    text
                }
                promoCodeId
                titleDescription
                discountAmount
                cashbackWalletAmount
                cashbackAdvocateReferralAmount
                invoiceDescription
                isCoupon
                gatewayId
                tickerInfo{
                    uniqueId
                    statusCode
                    message
                }
            }
        }
    }"""

    fun promoCheckPromoCode() = """
    mutation check_promo_cart_v2(${'$'}promoCode: String!, ${'$'}skipApply: Boolean!, ${'$'}suggested : Boolean!, ${'$'}oneClickShipment : Boolean!){
        check_promo_cart_v2(promoCode: ${'$'}promoCode, skipApply: ${'$'}skipApply, suggested : ${'$'}suggested, oneClickShipment : ${'$'}oneClickShipment){
            status
            error_message
            data{
                error
                data_voucher{
                    code
                    promo_code_id
                    discount_amount
                    cashback_amount
                    saldo_amount
                    cashback_top_cash_amount
                    cashback_voucher_amount
                    cashback_advocate_referral_amount
                    extra_amount
                    cashback_voucher_description
                    lp
                    gateway_id
                    message{
                        state
                        color
                        text
                    }
                    is_coupon
                    title_description
                }
            }
        }
    }"""

    fun promoCheckPromoCodeFinalPromoStacking() = """
    mutation get_promo_stack_use(${'$'}params: PromoStackRequest) {
        get_promo_stack_use(params: ${'$'}params) {
            status
            data {
                global_success
                success
                codes
                message {
                    state
                    color
                    text
                }
                promo_code_id
                title_description
                discount_amount
                cashback_wallet_amount
                cashback_advocate_referral_amount
                cashback_voucher_description
                invoice_description
                gateway_id
                is_coupon
                tracking_details {
                    product_id
                    promo_codes_tracking
                    promo_details_tracking
                }
                coupon_description
                benefit_summary_info {
                    final_benefit_text
                    final_benefit_amount
                    final_benefit_amount_str
                    summaries {
                        description
                        type
                        amount_str
                        amount
                        details {
                            description
                            type
                            amount_str
                            amount
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
                            potential_benefit
                            promo_name
                            unique_id
                        }
                    }
                }
                voucher_orders {
                    code
                    success
                    unique_id
                    cart_id
                    type
                    cashback_wallet_amount
                    discount_amount
                    title_description
                    invoice_description
                    message {
                        state
                        color
                        text
                    }
                }
                ticker_info {
                    unique_id
                    status_code
                    message
                }
            }
        }
    }"""

    fun promoCheckPromoCodePromoStackingFirst() = """
    mutation get_promo_stack_first(${'$'}params: PromoStackRequest) {
        get_promo_stack_first(params: ${'$'}params) {
            status
            data {
                global_success
                success
                codes
                message {
                    state
                    color
                    text
                }
                promo_code_id
                title_description
                discount_amount
                cashback_wallet_amount
                cashback_advocate_referral_amount
                cashback_voucher_description
                invoice_description
                gateway_id
                is_coupon
                tracking_details {
                    product_id
                    promo_codes_tracking
                    promo_details_tracking
                }
                coupon_description
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
                voucher_orders {
                    code
                    success
                    unique_id
                    cart_id
                    type
                    cashback_wallet_amount
                    discount_amount
                    title_description
                    invoice_description
                    message {
                        state
                        color
                        text
                    }
                }
            }
        }
    }"""

    fun clearCacheAutoApplyStack()="""
            mutation {
                clearCacheAutoApplyStack(
                        serviceID: "#serviceId",
                promoCode: #promoCode,
                isOCC: #isOCC) {
                Success
                ticker_message
            }
            }"""
}