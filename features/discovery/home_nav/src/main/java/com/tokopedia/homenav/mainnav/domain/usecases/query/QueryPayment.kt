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
        "query PaymentQuery() {" +
        "  paymentQuery: paymentList(lang: \"ID\", cursor:\"\") {" +
        "    paymentList: payment_list {" +
        "      merchantCode: merchant_code" +
        "      transactionID: transaction_id" +
        "      paymentAmount: payment_amount" +
        "      tickerMessage: ticker_message" +
        "      gatewayImg: gateway_img" +
        "      applink: app_link" +
        "      bankImg: bank_img" +
        "    }" +
        "  }" +
        "}"
}
