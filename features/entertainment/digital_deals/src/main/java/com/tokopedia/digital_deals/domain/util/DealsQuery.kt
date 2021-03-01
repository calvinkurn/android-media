package com.tokopedia.digital_deals.domain.util

object DealsQuery {
    fun eventContentById() = """
        query (${'$'}typeID:String!, ${'$'}typeValue:String!){
            event_content_by_id (QueryInput: {typeID: ${'$'}typeID, typeValue: ${'$'}typeValue}) {
               data {
                 section_data {
                 content {
                    value_text
                   }
                 }
                }
            }
        }""".trimIndent()
}