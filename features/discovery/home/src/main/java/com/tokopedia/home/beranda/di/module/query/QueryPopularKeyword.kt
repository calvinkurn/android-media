package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryPopularKeyword.POPULAR_KEYWORD_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryPopularKeyword.POPULAR_KEYWORD_QUERY_NAME

@GqlQuery(POPULAR_KEYWORD_QUERY_NAME, POPULAR_KEYWORD_QUERY)
internal object QueryPopularKeyword {
    const val POPULAR_KEYWORD_QUERY_NAME = "PopularKeywordGqlQuery"
    const val POPULAR_KEYWORD_QUERY = "query PopularKeywords(\$count: Int!, \$page: Int!) {\n" +
            "                popular_keywords(count: \$count, page: \$page) {\n" +
            "                    recommendation_type\n" +
            "                    title\n" +
            "                    sub_title\n" +
            "                    keywords {\n" +
            "                      url\n" +
            "                      image_url\n" +
            "                      keyword\n" +
            "                      mobile_url\n" +
            "                      product_count\n" +
            "                      product_count_formatted\n" +
            "                    }\n" +
            "                }\n" +
            "            }"
}