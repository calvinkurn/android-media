package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryBusinessUnitList.BUSINESS_UNIT_LIST_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryBusinessUnitList.BUSINESS_UNIT_LIST_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(BUSINESS_UNIT_LIST_QUERY_NAME, BUSINESS_UNIT_LIST_QUERY)
internal object QueryBusinessUnitList {
    const val BUSINESS_UNIT_LIST_QUERY_NAME = "BusinessUnitListQuery"
    const val BUSINESS_UNIT_LIST_QUERY = "" +
        "query businessUnitList(${'$'}page:String){\n" +
        "              dynamicHomeIcon{\n" +
        "                categoryGroup(page:${'$'}page){\n" +
        "                  id\n" +
        "                  title\n" +
        "                  imageUrl\n" +
        "                  applink\n" +
        "                  url\n" +
        "                  categoryRows{\n" +
        "                    id\n" +
        "                    name\n" +
        "                    imageUrl\n" +
        "                    applinks\n" +
        "                    url\n" +
        "                  }\n" +
        "                }\n" +
        "              }\n" +
        "            }"
}
