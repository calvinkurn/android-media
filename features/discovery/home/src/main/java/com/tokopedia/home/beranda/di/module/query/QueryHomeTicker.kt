package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeTicker.HOME_TICKER_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryHomeTicker.HOME_TICKER_QUERY_NAME

@Deprecated("Has been migrated to QueryHomeTickerV2")
@GqlQuery(HOME_TICKER_QUERY_NAME, HOME_TICKER_QUERY)
internal object QueryHomeTicker {
    const val HOME_TICKER_QUERY_NAME = "HomeTickerQuery"
    const val HOME_TICKER_QUERY: String = "" +
            "query homeTicker(\$location: String)\n" +
            "        {\n" +
            "          ticker {\n" +
            "            meta {\n" +
            "              total_data\n" +
            "            }\n" +
            "            tickers(location: \$location)\n" +
            "            {\n" +
            "              id\n" +
            "              title\n" +
            "              message\n" +
            "              color\n" +
            "              layout\n" +
            "              ticker_type\n" +
            "              title\n" +
            "            }\n" +
            "          }\n" +
            "        }"
}
