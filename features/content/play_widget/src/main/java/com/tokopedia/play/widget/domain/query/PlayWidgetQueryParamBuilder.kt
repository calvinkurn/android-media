package com.tokopedia.play.widget.domain.query

import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import java.lang.Exception

/**
 * Created By : Jonathan Darwin on November 26, 2021
 */
object PlayWidgetQueryParamBuilder {

    private var queryName = "query playGetWidgetV2"
    private var paramField = ""
    private var paramRequest = ""
    private var body = ""

    fun setBody(body: String): PlayWidgetQueryParamBuilder {
        this.body = body
        return this
    }

    fun setWidgetType(widgetType: PlayWidgetUseCase.WidgetType?): PlayWidgetQueryParamBuilder {
        return widgetType?.let {
            paramField = generateField(widgetType)
            paramRequest = generateRequest(widgetType)
            this
        } ?: throw Exception("Please provide widget type")
    }

    private fun generateField(widgetType: PlayWidgetUseCase.WidgetType): String {
        var field = ""

        field += "("
        when(widgetType) {
            is PlayWidgetUseCase.WidgetType.PDPWidget -> {
                field += """
                    ${'$'}$PARAM_WIDGET_TYPE: String!, 
                    ${'$'}$PARAM_AUTHOR_ID: String, 
                    ${'$'}$PARAM_AUTHOR_TYPE: String,
                    ${'$'}$PARAM_IS_WIFI: Boolean
                """.trimIndent()
            }
            else -> {
                field += """
                    ${'$'}$PARAM_WIDGET_TYPE: String!, 
                    ${'$'}$PARAM_AUTHOR_ID: String, 
                    ${'$'}$PARAM_AUTHOR_TYPE: String,
                    ${'$'}$PARAM_IS_WIFI: Boolean,
                    ${'$'}$PARAM_PRODUCT_ID: String,
                    ${'$'}$PARAM_CATEGORY_ID: String
                """.trimIndent()
            }
        }
        field += ")"

        return field
    }

    private fun generateRequest(widgetType: PlayWidgetUseCase.WidgetType): String {
        var request = ""

        request += "{"

        when(widgetType) {
            is PlayWidgetUseCase.WidgetType.PDPWidget -> {
                request += """
                    playGetWidgetV2(
                        req: {
                          ${PARAM_WIDGET_TYPE}:${'$'}${PARAM_WIDGET_TYPE},
                          ${PARAM_AUTHOR_ID}: ${'$'}${PARAM_AUTHOR_ID},
                          ${PARAM_AUTHOR_TYPE}: ${'$'}${PARAM_AUTHOR_TYPE},
                          ${PARAM_IS_WIFI}: ${'$'}${PARAM_IS_WIFI},
                          ${PARAM_PRODUCT_ID}: ${'$'}${PARAM_PRODUCT_ID},
                          ${PARAM_CATEGORY_ID}: ${'$'}${PARAM_CATEGORY_ID}
                        })
                """.trimIndent()
            }
            else -> {
                request += """
                    playGetWidgetV2(
                        req: {
                          ${PARAM_WIDGET_TYPE}:${'$'}${PARAM_WIDGET_TYPE},
                          ${PARAM_AUTHOR_ID}: ${'$'}${PARAM_AUTHOR_ID},
                          ${PARAM_AUTHOR_TYPE}: ${'$'}${PARAM_AUTHOR_TYPE},
                          ${PARAM_IS_WIFI}: ${'$'}${PARAM_IS_WIFI}
                        })
                """.trimIndent()
            }
        }

        request += "}"

        return request
    }

    fun build(): String {
        return buildString {
            append(queryName)
            append("(")
                append(paramField)
            append(")")
            append("{")
                append(paramRequest)
                append(body)
            append("}")
        }
    }

    const val PARAM_WIDGET_TYPE = "widgetType"
    const val PARAM_AUTHOR_ID = "authorID"
    const val PARAM_AUTHOR_TYPE = "authorType"
    const val PARAM_IS_WIFI = "isWifi"
    const val PARAM_PRODUCT_ID = "productIDs"
    const val PARAM_CATEGORY_ID = "categoryIDs"
}