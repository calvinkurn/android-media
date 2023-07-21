package com.tokopedia.digital_checkout.data

/**
 * @author by jessica on 08/01/21
 */

object DigitalCheckoutQueries {

    const val RECHARGE_GET_CART_QUERY= """
        query rechargeGetCart(${'$'}categoryId: Int!) {
          rechargeGetCart(CategoryID: ${'$'}categoryId) {
            id
            product_id
            user_id
            client_number
            title
            category_name
            operator_name
            icon
            price_text
            price
            is_instant_checkout
            is_otp_required
            sms_state
            voucher
            is_open_payment
            open_payment_config {
              min_payment
              max_payment
              min_payment_text
              max_payment_text
              min_payment_error_text
              max_payment_error_text
            }
            main_info {
              label
              value
            }
            additional_info {
              title
              detail {
                label
                value
              }
            }
            enable_voucher
            is_coupon_active
            auto_apply {
              success
              message {
                state
                color
                text
              }
                code
              is_coupon
              discount_amount
              discount_amount_label
              discount_price
              discounted_amount
              discounted_price
              title_description
              promo_code_id
              promo_id
              message_success
            }
            default_promo
            pop_up {
              title
              content
              image_url
              action {
                yes_button_title
              }
            }
            fintech_products {
              id
              transaction_type
              tier_id
              opt_in
              check_box_disabled
              allow_ovo_points
              fintech_amount
              fintech_partner_amount
              operator_name
              cross_sell_metadata
              info {
                title
                subtitle
                checked_subtitle
                link_text
                link_url
                tooltip_text
                icon_url
              }
            }
            atc_source
            admin_fee_text
            admin_fee
            admin_fee_included
            channel_id
            collection_point_id
            collection_point_version
          }
        }
    """

    const val CANCEL_VOUCHER_CHART_QUERY = """
        mutation clearCacheAutoApplyStack(${'$'}serviceID: String!, ${'$'}promoCode:[String], ${'$'}isOCC: Boolean) {
          clearCacheAutoApplyStack(serviceID:${'$'}serviceID, promoCode: ${'$'}promoCode, isOCC: ${'$'}isOCC) { 
            Success
            ticker_message
            default_empty_promo_message
            error
          }
        }
    """
}
