package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryAtf.ATF_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryAtf.ATF_QUERY_NAME

@GqlQuery(ATF_QUERY_NAME, ATF_QUERY)
internal object QueryAtf {
    const val ATF_QUERY_NAME = "AtfQuery"
    const val ATF_QUERY = "query getAtf(\$experiment: String!, \$variant: String!) {\n" +
        "  dynamicPosition(experiment: \$experiment, variant: \$variant){\n" +
        "    id\n" +
        "    name\n" +
        "    component\n" +
        "    param\n" +
        "    isOptional\n" +
        "    isShimmer\n" +
        "    }\n" +
        "}"
}
