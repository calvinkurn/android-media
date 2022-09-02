package com.tokopedia.travel.country_code

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * created by @bayazidnasir on 25/7/2022
 */

class DummyGqlQueryInterface: GqlQueryInterface {

    override fun getOperationNameList(): List<String> = emptyList()

    override fun getQuery(): String = ""

    override fun getTopOperationName(): String = ""
}