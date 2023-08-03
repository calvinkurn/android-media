package com.tokopedia.play.widget.domain.query

import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import java.lang.Exception

/**
 * Created By : Jonathan Darwin on November 26, 2021
 */
class PlayWidgetQueryParamBuilder {

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
        return buildString {
            appendLine("${'$'}$PARAM_WIDGET_TYPE: String!,")
            appendLine("${'$'}$PARAM_AUTHOR_ID: String,")
            appendLine("${'$'}$PARAM_AUTHOR_TYPE: String,")
            append("${'$'}$PARAM_IS_WIFI: Boolean")

            when (widgetType) {
                is PlayWidgetUseCase.WidgetType.PDPWidget -> {
                    appendLine(",")
                    appendLine("${'$'}$PARAM_PRODUCT_ID: String,")
                    appendLine("${'$'}$PARAM_CATEGORY_ID: String")
                }
                is PlayWidgetUseCase.WidgetType.ShopPageExclusiveLaunch -> {
                    appendLine(",")
                    appendLine("${'$'}$PARAM_CAMPAIGN_ID: String")
                }
                else -> {
                    //do nothing with other widget type
                }
            }
        }
    }

    private fun generateRequest(widgetType: PlayWidgetUseCase.WidgetType): String {
        return buildString {
            appendLine("playGetWidgetV2(")
            appendLine("req: {")

            appendLine("${PARAM_WIDGET_TYPE}:${'$'}${PARAM_WIDGET_TYPE},")
            appendLine("${PARAM_AUTHOR_ID}: ${'$'}${PARAM_AUTHOR_ID},")
            appendLine("${PARAM_AUTHOR_TYPE}: ${'$'}${PARAM_AUTHOR_TYPE},")
            append("${PARAM_IS_WIFI}: ${'$'}${PARAM_IS_WIFI}")

            when (widgetType) {
                is PlayWidgetUseCase.WidgetType.PDPWidget -> {
                    appendLine(",")
                    appendLine("${PARAM_PRODUCT_ID}: ${'$'}${PARAM_PRODUCT_ID},")
                    appendLine("${PARAM_CATEGORY_ID}: ${'$'}${PARAM_CATEGORY_ID}")
                }
                is PlayWidgetUseCase.WidgetType.ShopPageExclusiveLaunch -> {
                    appendLine(",")
                    appendLine("${PARAM_CAMPAIGN_ID}: ${'$'}${PARAM_CAMPAIGN_ID}")
                }
                else -> {
                    //do nothing with other widget type
                }
            }

            appendLine("})")
        }
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

    companion object {
        const val PARAM_WIDGET_TYPE = "widgetType"
        const val PARAM_AUTHOR_ID = "authorID"
        const val PARAM_AUTHOR_TYPE = "authorType"
        const val PARAM_IS_WIFI = "isWifi"
        const val PARAM_PRODUCT_ID = "productIDs"
        const val PARAM_CATEGORY_ID = "categoryIDs"
        const val PARAM_CHANNEL_TAG = "channelTag"
        const val PARAM_CAMPAIGN_ID = "campaignID"
    }
}
