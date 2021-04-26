package com.tokopedia.digital_checkout.data

/**
 * @author by jessica on 08/01/21
 */

object DigitalCheckoutQueries {

    fun getGetCartQuery() = """
        query (${'$'}categoryId: Int!) {
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
              discount_price
              discounted_amount
              discounted_price
              title_description
              promo_code_id
              promo_id
              message_success
            }
            default_promo
            cross_selling_type
            cross_selling_config {
              can_be_skipped
              is_checked
              wording {
                header_title
                body_title
                body_content_before
                body_content_after
                cta_button_text
              }
              wording_is_subscribed {
                header_title
                body_title
                body_content_before
                body_content_after
                cta_button_text
              }
            }
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
              info {
                title
                subtitle
                link_text
                link_url
                tooltip_text
                icon_url
              }
            }
            atc_source
            admin_fee_text
            admin_fee
          }
        }
    """.trimIndent()

    fun getCancelVoucherCartQuery() = """
        mutation {
          clearCacheAutoApplyV2(serviceID: "819380128012836") {
            Success
          }
        }
    """.trimIndent()
}