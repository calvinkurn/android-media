package com.tokopedia.travel.passenger.presentation.viewmodel

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * created by @bayazidnasir on 25/7/2022
 */
class TravelGqlInterface: GqlQueryInterface {

    override fun getOperationNameList(): List<String> = emptyList()

    override fun getQuery(): String = ""

    override fun getTopOperationName(): String = ""
}