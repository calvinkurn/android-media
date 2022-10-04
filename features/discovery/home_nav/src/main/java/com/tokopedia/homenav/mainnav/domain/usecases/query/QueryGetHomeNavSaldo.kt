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
        "query getHomeNavSaldo() {" +
        "  balance {" +
        "    buyer_hold" +
        "    buyer_hold_fmt" +
        "    buyer_usable" +
        "    buyer_usable_fmt" +
        "    seller_hold" +
        "    seller_hold_fmt" +
        "    seller_usable" +
        "    seller_usable_fmt" +
        "    have_error" +
        "  }" +
        "}"
}
