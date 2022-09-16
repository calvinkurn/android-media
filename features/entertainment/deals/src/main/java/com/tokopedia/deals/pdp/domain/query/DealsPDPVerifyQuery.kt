package com.tokopedia.deals.pdp.domain.query

import com.tokopedia.deals.pdp.data.DealsVerifyRequest
import com.tokopedia.deals.pdp.domain.query.DealsPDPVerifyQuery.DEALS_PDP_VERIFY_OPERATION_NAME
import com.tokopedia.deals.pdp.domain.query.DealsPDPVerifyQuery.DEALS_PDP_VERIFY_QUERY
import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(
    DEALS_PDP_VERIFY_OPERATION_NAME,
    DEALS_PDP_VERIFY_QUERY
)
object DealsPDPVerifyQuery {
    const val DEALS_PDP_VERIFY_OPERATION_NAME = "verifyV2Query"
    const val DEALS_PDP_VERIFY_QUERY = """
        mutation $DEALS_PDP_VERIFY_OPERATION_NAME(${'$'}eventVerify: VerifyRequest!) {
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
    }
    """

    private const val VERIFY_KEY = "eventVerify"

    @JvmStatic
    fun createRequestParam(dealsVerifyRequest: DealsVerifyRequest) = HashMap<String, Any>().apply {
        put(VERIFY_KEY, dealsVerifyRequest)
    }
}
