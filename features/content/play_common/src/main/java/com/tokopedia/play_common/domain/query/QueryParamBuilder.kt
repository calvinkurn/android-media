package com.tokopedia.play_common.domain.query


/**
 * Created by mzennis on 06/11/20.
 */
class QueryParamBuilder {

    private var mParams = emptyMap<String, Any>()
    private var mFields = listOf<FieldsToUpdate>()

    fun setParams(params: Map<String, Any>): QueryParamBuilder {
        mParams = params
        return this
    }

    fun setFields(fields: List<FieldsToUpdate>): QueryParamBuilder {
        mFields = fields
        return this
    }

    fun build(): QueryParams = QueryParams(
            params = mParams,
            query = buildQueryString(mFields)
    )

    private fun buildQueryString(fields: List<FieldsToUpdate>): String {
        return buildString {
            append("mutation UpdateChannel(${'$'}channelId: String!")
            fields.forEach { field ->
                append(", ${'$'}")
                append(field.fieldName)
                append(": ")
                append(field.gqlType)
            }
            append("){")
            appendln()
            appendln("broadcasterUpdateChannel(")
            appendln(buildRequestString(fields))
            appendln("""
                        ) {
                    channelID
                  }
                }
                """)
        }
    }

    private fun buildRequestString(fields: List<FieldsToUpdate>): String {
        return buildString {
            appendln("req : {")

            appendln("channelID: ${'$'}channelId,")

            append("fieldsToUpdate: [")
            append(fields.joinToString { "\"${it.fieldName}\"" })
            appendln("],")

            fields.forEach { field ->
                append(field.fieldName)
                append(": ")
                append("${'$'}")
                append(field.fieldName)
                appendln(",")
            }

            appendln("}")
        }
    }
}