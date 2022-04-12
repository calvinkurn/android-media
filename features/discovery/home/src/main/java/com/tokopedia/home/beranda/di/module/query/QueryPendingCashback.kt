package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryPendingCashback.PENDING_CASHBACK_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryPendingCashback.PENDING_CASHBACK_QUERY_NAME

@GqlQuery(PENDING_CASHBACK_QUERY_NAME, PENDING_CASHBACK_QUERY)
internal object QueryPendingCashback {
    const val PENDING_CASHBACK_QUERY_NAME = "PendingCashbackQuery"
    const val PENDING_CASHBACK_QUERY : String = "query pendingCashback() {\n" +
            "  goalPendingBalance {\n" +
            "    balance\n" +
            "    balance_text\n" +
            "    cash_balance\n" +
            "    cash_balance_text\n" +
            "    point_balance\n" +
            "    point_balance_text\n" +
            "    wallet_type\n" +
            "    phone_number\n" +
            "    errors {\n" +
            "      title\n" +
            "      message\n" +
            "    }\n" +
            "  }\n" +
            "}\n"
}