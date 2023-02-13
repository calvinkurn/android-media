package com.tokopedia.deals.checkout.domain.query

import com.tokopedia.deals.checkout.domain.query.DealsCheckoutGeneralV2Query.DEALS_CHECKOUT_GENERAL_V2_NAME
import com.tokopedia.deals.checkout.domain.query.DealsCheckoutGeneralV2Query.DEALS_CHECKOUT_GENERAL_V2_QUERY
import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(DEALS_CHECKOUT_GENERAL_V2_NAME, DEALS_CHECKOUT_GENERAL_V2_QUERY)
object DealsCheckoutGeneralV2Query {
    const val DEALS_CHECKOUT_GENERAL_V2_NAME = "checkoutGeneralV2Mutation"
    const val DEALS_CHECKOUT_GENERAL_V2_QUERY = """
        mutation $DEALS_CHECKOUT_GENERAL_V2_NAME(${'$'}params:CheckoutGeneralV2Params!){
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
    }
    """
}
