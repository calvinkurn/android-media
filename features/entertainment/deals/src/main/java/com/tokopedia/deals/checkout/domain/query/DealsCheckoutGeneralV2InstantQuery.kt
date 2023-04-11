package com.tokopedia.deals.checkout.domain.query

import com.tokopedia.deals.checkout.domain.query.DealsCheckoutGeneralV2InstantQuery.DEALS_CHECKOUT_GENERAL_V2_INSTANT_NAME
import com.tokopedia.deals.checkout.domain.query.DealsCheckoutGeneralV2InstantQuery.DEALS_CHECKOUT_GENERAL_V2_INSTANT_QUERY
import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(DEALS_CHECKOUT_GENERAL_V2_INSTANT_NAME, DEALS_CHECKOUT_GENERAL_V2_INSTANT_QUERY)
object DealsCheckoutGeneralV2InstantQuery {
    const val DEALS_CHECKOUT_GENERAL_V2_INSTANT_NAME = "checkoutGeneralV2InstantMutation"
    const val DEALS_CHECKOUT_GENERAL_V2_INSTANT_QUERY = """
        mutation $DEALS_CHECKOUT_GENERAL_V2_INSTANT_NAME(${'$'}params: CheckoutGeneralV2InstantParams) {
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
        }
    """
}
