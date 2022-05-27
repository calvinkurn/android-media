package com.tokopedia.officialstore.official.di.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.officialstore.official.di.query.QueryOSCategories.OS_CATEGORIES_QUERY
import com.tokopedia.officialstore.official.di.query.QueryOSCategories.OS_CATEGORIES_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(OS_CATEGORIES_QUERY_NAME, OS_CATEGORIES_QUERY)
internal object QueryOSCategories {
    const val OS_CATEGORIES_QUERY_NAME = "OSCategoriesQuery"
    const val OS_CATEGORIES_QUERY = "query OfficialStoreCategories(){\n" +
            "  OfficialStoreCategories(lang: \"id\") {\n" +
            "    categories {\n" +
            "        id\n" +
            "        imageUrl\n" +
            "        name\n" +
            "        prefixUrl\n" +
            "        url\n" +
            "        fullUrl\n" +
            "        categories\n" +
            "        imageInactiveURL\n" +
            "    }\n" +
            "    total\n" +
            "  }\n" +
            "}"
}