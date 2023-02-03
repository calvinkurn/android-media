package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeTickerV2.HOME_TICKER_V2_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryHomeTickerV2.HOME_TICKER_V2_QUERY_NAME

@GqlQuery(HOME_TICKER_V2_QUERY_NAME, HOME_TICKER_V2_QUERY)
internal object QueryHomeTickerV2 {
    const val HOME_TICKER_V2_QUERY_NAME = "HomeTickerV2Query"
    const val HOME_TICKER_V2_QUERY: String = "" +
            "query getHomeTickerV2(\$location: String) {\n" +
            "  getHomeTickerV2(location: \$location) {\n" +
            "    tickers {\n" +
            "      id\n" +
            "      title\n" +
            "      color\n" +
            "      layout\n" +
            "      message\n" +
            "      tickerType\n" +
            "    }\n" +
            "  }\n" +
            "}\n"
}
