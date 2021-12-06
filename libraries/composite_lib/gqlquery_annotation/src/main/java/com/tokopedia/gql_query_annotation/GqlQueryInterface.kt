package com.tokopedia.gql_query_annotation

interface GqlQueryInterface {
    fun getQuery(): String
    fun getOperationNameList(): List<String>
    fun getTopOperationName(): String
}