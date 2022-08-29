package com.tokopedia.deals.pdp.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object DealsPDPContentQuery: GqlQueryInterface {
    private const val OPERATION_NAME = "EventContentById"
    private val QUERY = """
        query $OPERATION_NAME(${'$'}typeID:String!, ${'$'}typeValue:String!){
            event_content_by_id (QueryInput: {typeID: ${'$'}typeID, typeValue: ${'$'}typeValue}) {
               data {
                 section_data {
                 content {
                    value_text
                   }
                 }
                }
            }
        }
    """.trimIndent()

    private const val TYPE_ID = "typeID"
    private const val TYPE_VALUE = "typeValue"

    @JvmStatic
    fun createRequestParams(typeId: String, typeValue: String) = HashMap<String, Any>().apply {
        put(TYPE_ID, typeId)
        put(TYPE_VALUE, typeValue)
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
    override fun getQuery(): String = QUERY
    override fun getTopOperationName(): String = OPERATION_NAME
}