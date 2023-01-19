package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryCategoryV2.CATEGORY_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryCategoryV2.CATEGORY_QUERY_NAME

/**
 * Created by frenzel 16/11/22
 */
@GqlQuery(CATEGORY_QUERY_NAME, CATEGORY_QUERY)
internal object QueryCategoryV2 {
    const val CATEGORY_QUERY_NAME = "CategoryQuery"
    const val CATEGORY_QUERY = "" +
        "query getHomeCategoryV2(\$page: String) {\n" +
        "  getHomeCategoryV2(page: \$page) {\n" +
        "    categories {\n" +
        "      id\n" +
        "      url\n" +
        "      title\n" +
        "      applink\n" +
        "      imageUrl\n" +
        "      categoryRows {\n" +
        "        id\n" +
        "        url\n" +
        "        name\n" +
        "        imageUrl\n" +
        "        applinks\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}"
}
