package com.tokopedia.digital.home.presentation.viewmodel

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * created by @bayazidnasir on 9/8/2022
 */
class DummyDigitalQueryInterface: GqlQueryInterface {

    override fun getOperationNameList(): List<String> = emptyList()

    override fun getQuery(): String = ""

    override fun getTopOperationName(): String = ""
}