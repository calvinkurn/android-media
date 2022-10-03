package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryPayment.PAYMENT_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryPayment.PAYMENT_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(PAYMENT_QUERY_NAME, PAYMENT_QUERY)
object QueryPayment {
    const val PAYMENT_QUERY_NAME = "GetPaymentQuery"
    const val PAYMENT_QUERY = "" +
        "query PaymentQuery() {\n" +
        "  paymentQuery: paymentList(lang: \"ID\", cursor:\"\") {\n" +
        "    paymentList: payment_list {\n" +
        "      merchantCode: merchant_code\n" +
        "      transactionID: transaction_id\n" +
        "      paymentAmount: payment_amount\n" +
        "      tickerMessage: ticker_message\n" +
        "      gatewayImg: gateway_img\n" +
        "      applink: app_link\n" +
        "      bankImg: bank_img\n" +
        "    }\n" +
        "  }\n" +
        "}"
}
