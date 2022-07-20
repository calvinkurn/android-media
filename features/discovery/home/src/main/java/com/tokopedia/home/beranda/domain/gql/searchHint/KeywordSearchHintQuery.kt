package com.tokopedia.home.beranda.domain.gql.searchHint

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.domain.gql.searchHint.KeywordSearchHintQuery.KEYWORD_SEARCH_HINT_QUERY
import com.tokopedia.home.beranda.domain.gql.searchHint.KeywordSearchHintQuery.KEYWORD_SEARCH_HINT_QUERY_NAME

@GqlQuery(KEYWORD_SEARCH_HINT_QUERY_NAME, KEYWORD_SEARCH_HINT_QUERY)
internal object KeywordSearchHintQuery {
    private const val firstInstall = "\$firstInstall"
    private const val uniqueId = "\$uniqueId"

    const val KEYWORD_SEARCH_HINT_QUERY_NAME = "KeywordSearchHintUniversalQuery"
    const val KEYWORD_SEARCH_HINT_QUERY = "query universeplaceholder($firstInstall: Boolean, $uniqueId:String){\n" +
            "            universe_placeholder(navsource:\"home\", first_install:$firstInstall, unique_id:$uniqueId){\n" +
            "                data { \n" +
            "                    placeholder \n" +
            "                    keyword \n" +
            "                    placeholder_list {\n" +
            "                        placeholder\n" +
            "                        keyword\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }"
}