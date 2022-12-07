package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetMembership.GET_MEMBERSHIP_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetMembership.GET_MEMBERSHIP_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(GET_MEMBERSHIP_QUERY_NAME, GET_MEMBERSHIP_QUERY)
internal object QueryGetMembership {
    const val GET_MEMBERSHIP_QUERY_NAME = "GetMembershipQuery"
    const val GET_MEMBERSHIP_QUERY = "" +
        "query getMembership(){\n" +
        "      tokopoints {\n" +
        "        status {\n" +
        "          tier {\n" +
        "            id\n" +
        "            name\n" +
        "            nameDesc\n" +
        "            eggImageURL\n" +
        "            eggImageHomepageURL\n" +
        "          }\n" +
        "        }\n" +
        "      }\n" +
        "}"
}
