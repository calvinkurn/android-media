package com.tokopedia.officialstore.official.di.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.officialstore.official.di.query.QueryOSBenefits.OS_BENEFIT_QUERY
import com.tokopedia.officialstore.official.di.query.QueryOSBenefits.OS_BENEFIT_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(OS_BENEFIT_QUERY_NAME, OS_BENEFIT_QUERY)
internal object QueryOSBenefits {
    const val OS_BENEFIT_QUERY_NAME = "OSBenefitsQuery"
    const val OS_BENEFIT_QUERY = "query OfficialStoreBenefits() {\n" +
            "  OfficialStoreBenefits {\n" +
            "    benefits{\n" +
            "      id\n" +
            "      label\n" +
            "      iconUrl\n" +
            "      position\n" +
            "      redirectUrl\n" +
            "    }\n" +
            "  }\n" +
            "}"
}