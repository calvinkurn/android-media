package com.tokopedia.flight.dummy

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * created by @bayazidnasir on 25/7/2022
 */

class FlightDummyGqlInterfaceImpl: GqlQueryInterface {

    override fun getOperationNameList(): List<String> = emptyList()

    override fun getQuery(): String = ""

    override fun getTopOperationName(): String = ""
}