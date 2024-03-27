package com.tokopedia.developer_options.mock_dynamic_widget.shop_page

import com.google.gson.JsonParser
import com.tokopedia.kotlin.extensions.orFalse

data class ShopPageMockWidgetModel(
    private var mockShopWidgetData: Pair<String, String>
) {
    fun getWidgetName(): String {
        val jsonObjectData = JsonParser.parseString(mockShopWidgetData.first).asJsonObject
        return jsonObjectData?.get("widgetName")?.asString.orEmpty()
    }

    fun getWidgetType(): String {
        val jsonObjectData = JsonParser.parseString(mockShopWidgetData.first).asJsonObject
        return jsonObjectData?.get("widgetType")?.asString.orEmpty()
    }

    fun editWidgetId(index: Int) {
        val first = JsonParser.parseString(mockShopWidgetData.first).asJsonObject.apply {
            addProperty("widgetID", index)
        }.toString()
        val second = JsonParser.parseString(mockShopWidgetData.second).asJsonObject.apply {
            addProperty("widgetID", index)
        }.toString()
        mockShopWidgetData = Pair(first, second)
    }

    fun getMockShopWidgetData(): Pair<String, String> {
        return mockShopWidgetData
    }

    fun updateIsFestivity(isFestivity: Boolean) {
        val first = JsonParser.parseString(mockShopWidgetData.first).asJsonObject.apply {
            addProperty("isFestivity", isFestivity)
        }.toString()
        val second = JsonParser.parseString(mockShopWidgetData.second).asJsonObject.apply {
            addProperty("isFestivity", isFestivity)
        }.toString()
        mockShopWidgetData = Pair(first, second)
    }

    fun isFestivity(): Boolean {
        return JsonParser.parseString(mockShopWidgetData.first).asJsonObject?.get("isFestivity")?.asBoolean.orFalse()
    }

    data class BmsmMockWidgetModel(
        var mockBmsmWidgetData: Pair<String, String> = Pair("", "")
    )
}
