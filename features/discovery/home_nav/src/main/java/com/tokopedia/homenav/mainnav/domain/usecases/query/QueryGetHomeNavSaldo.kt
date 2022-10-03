package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetHomeNavSaldo.GET_HOME_NAV_SALDO_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetHomeNavSaldo.GET_HOME_NAV_SALDO_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(GET_HOME_NAV_SALDO_QUERY_NAME, GET_HOME_NAV_SALDO_QUERY)
internal object QueryGetHomeNavSaldo {
    const val GET_HOME_NAV_SALDO_QUERY_NAME = "GetHomeNavSaldoQuery"
    const val GET_HOME_NAV_SALDO_QUERY = "" +
        "query getHomeNavSaldo() {\n" +
        "  balance {\n" +
        "    buyer_hold\n" +
        "    buyer_hold_fmt\n" +
        "    buyer_usable\n" +
        "    buyer_usable_fmt\n" +
        "    seller_hold\n" +
        "    seller_hold_fmt\n" +
        "    seller_usable\n" +
        "    seller_usable_fmt\n" +
        "    have_error\n" +
        "  }\n" +
        "}"
}
