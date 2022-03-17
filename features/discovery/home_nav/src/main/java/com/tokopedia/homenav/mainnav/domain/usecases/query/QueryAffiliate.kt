package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryAffiliate.AFFILIATE_USER_DETAIL_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryAffiliate.AFFILIATE_USER_DETAIL_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(AFFILIATE_USER_DETAIL_QUERY_NAME, AFFILIATE_USER_DETAIL_QUERY)
object QueryAffiliate {
    const val AFFILIATE_USER_DETAIL_QUERY_NAME = "AffiliateUserDetailQuery"
    const val AFFILIATE_USER_DETAIL_QUERY = "query affiliateUserDetail (){\n" +
            "    IsRegistered\n" +
            "    Title\n" +
            "    Redirection {\n" +
            "      Android\n" +
            "    }\n" +
            "  }"
}