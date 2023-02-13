package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetProfile.GET_PROFILE_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetProfile.GET_PROFILE_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(GET_PROFILE_QUERY_NAME, GET_PROFILE_QUERY)
object QueryGetProfile {
    const val GET_PROFILE_QUERY_NAME = "GetProfileQuery"
    const val GET_PROFILE_QUERY = "" +
        "query getProfile(){\n" +
        "      profile {\n" +
        "        full_name\n" +
        "        profilePicture\n" +
        "      }\n" +
        "}"
}
