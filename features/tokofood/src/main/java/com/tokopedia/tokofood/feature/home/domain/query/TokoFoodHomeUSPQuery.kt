package com.tokopedia.tokofood.feature.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object TokoFoodHomeUSPQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "tokofoodGetUSP"
    private val QUERY = """
        query $OPERATION_NAME() {
          tokofoodGetUSP() {
            list {
              iconURL
              title
              description
              formatted
            } 
            footer
            imageURL
          }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
    override fun getQuery(): String = QUERY
    override fun getTopOperationName(): String = OPERATION_NAME
}