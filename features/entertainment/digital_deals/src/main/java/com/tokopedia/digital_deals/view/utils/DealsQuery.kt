package com.tokopedia.digital_deals.view.utils

object DealsQuery {
    fun mutationVerifyV2()="""
        mutation verify_v2(${'$'}eventVerify: VerifyRequest!) {
        event_verify(verifyRequestParam: ${'$'}eventVerify)
        {
            error
            error_description
            status
            metadata{
                product_ids
                product_names
                provider_ids
                item_ids
                category_name
                quantity
                total_price
                item_map{
                    id
                    name
                    product_id
                    category_id
                    child_category_ids
                    provider_id
                    product_name
                    package_name
                    end_time
                    start_time
                    price
                    quantity
                    total_price
                    location_name
                    location_desc
                    product_app_url
                    web_app_url
                    product_web_url
                    product_image
                    flag_id
                    package_id
                    order_trace_id
                    error
                    email
                    mobile
                    schedule_timestamp
                    description
                    provider_ticket_id
                    provider_schedule_id
                    base_price
                    commission
                    commission_type
                    currency_price
                    passenger_forms{
                        passenger_informations{
                            id
                            product_id
                            name
                            title
                            value
                            element_type
                            help_text
                            required
                            validator_regex
                            error_message
                        }
                    }
                    invoice_item_id
                    provider_order_id
                    provider_invoice_code
                    provider_package_id
                    invoice_id
                    provider_invoice_code
                    invoice_status
                    payment_type
                    buttons{
                        label
                        action
                        color
                        text_color
                        body{
                            app_url
                            web_url
                            method
                            voucher_codes
                        }
                        metadata{
                            key
                            value
                        }
                    }
                }
                error
                order_title
                order_subTitle
                buttons{
                    label
                    action
                    color
                    text_color
                    border_color
                    body{
                        app_url
                        web_url
                        method
                        voucher_codes
                    }
                    metadata{
                        key
                        value
                    }
                }
            }
            gateway_code
        }
    }"""

    fun mutationDealsCheckoutV2()="""
            mutation checkout_general_v2(${'$'}params:CheckoutGeneralV2Params!){
        checkout_general_v2(params:${'$'}params) {
            header {
                process_time
                reason
                error_code
            }
            data {
                success
                error
                error_state
                message
                data{
                    callback_url
                    parameter{
                        amount
                    }
                    price_validation{
                        is_updated
                        message{
                            action
                            desc
                            title
                        }
                    }
                    product_list{
                        id
                        name
                        price
                        quantity
                    }
                    query_string
                    redirect_url
                }
            }
            status
            error_reporter {
                eligible
            }
        }
    }"""

    fun mutationDealsCheckoutInstant()="""
        mutation checkout_general_v2_instant(${'$'}params: CheckoutGeneralV2InstantParams) {
            checkout_general_v2_instant(params: ${'$'}params){
                header {
                    process_time
                    reason
                    messages
                    error_code
                 }
                data {
                    success
                    error
                    error_state
                    message
                    data {
                        redirect_url
                        method
                        content_type
                        payload
                    }
                }
                status
                error_reporter {
                eligible
                texts {
                submit_title
                submit_description
                submit_button
                cancel_button
                  }
             }
             }
        }"""
}