package com.tokopedia.shareexperience.data.repository

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ShareExGetSharePropertiesQuery : GqlQueryInterface {

    private val operationName = "getShareProperties"
    override fun getOperationNameList(): List<String> {
        return listOf(operationName)
    }

    override fun getQuery(): String = """
        
    """.trimIndent()

    override fun getTopOperationName(): String = operationName
}
