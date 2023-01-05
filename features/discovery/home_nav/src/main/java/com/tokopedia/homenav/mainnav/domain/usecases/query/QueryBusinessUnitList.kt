package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryBusinessUnitList.BUSINESS_UNIT_LIST_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryBusinessUnitList.BUSINESS_UNIT_LIST_QUERY_NAME

/**
 * Created by dhaba
 */
@Deprecated("Has been migrated to QueryCategoryV2")
@GqlQuery(BUSINESS_UNIT_LIST_QUERY_NAME, BUSINESS_UNIT_LIST_QUERY)
internal object QueryBusinessUnitList {
    const val BUSINESS_UNIT_LIST_QUERY_NAME = "BusinessUnitListQuery"
    const val BUSINESS_UNIT_LIST_QUERY = "" +
        "query businessUnitList(${'$'}page:String){" +
        "  dynamicHomeIcon{" +
        "    categoryGroup(page:${'$'}page){" +
        "      id" +
        "      title" +
        "      imageUrl" +
        "      applink" +
        "      url" +
        "      categoryRows{" +
        "        id" +
        "        name" +
        "        imageUrl" +
        "        applinks" +
        "        url" +
        "      }" +
        "    }" +
        "  }" +
        "}"
}
