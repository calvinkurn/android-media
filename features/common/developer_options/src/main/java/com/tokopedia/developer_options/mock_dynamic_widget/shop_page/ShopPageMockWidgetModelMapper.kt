package com.tokopedia.developer_options.mock_dynamic_widget.shop_page

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.mock_dynamic_widget.shop_page.ShopPageMockWidgetModel.*
import com.tokopedia.kotlin.extensions.view.orZero
import timber.log.Timber
import java.io.IOException
import java.io.InputStream

object ShopPageMockWidgetModelMapper {
    private val SHOP_PAGE_MOCK_WIDGET_DATA_RESOURCE = R.raw.shop_page_template_mock_widget
    private val BMSM_WIDGET_OFFERING_PRODUCT_DATA_SOURCE = R.raw.bmsm_widget_get_offering_product_mock_response
    private val gson by lazy {
        Gson()
    }

    fun generateTemplateShopWidgetData(shopPageMockJsonData: JsonArray?): List<ShopPageMockWidgetModel> {
        return shopPageMockJsonData?.map { mockDataItem ->
            generateMockShopWidgetModel(mockDataItem)
        } ?: listOf()
    }

    fun getShopPageMockJsonFromRaw(resources: Resources): JsonArray? {
        val rawResource: InputStream = resources.openRawResource(SHOP_PAGE_MOCK_WIDGET_DATA_RESOURCE)
        val content: String = GraphqlHelper.streamToString(rawResource)
        try {
            rawResource.close()
        } catch (e: IOException) {
            Timber.e(e)
        }
        return JsonParser.parseString(content).asJsonArray
    }

    private fun generateMockShopWidgetModel(member: JsonElement): ShopPageMockWidgetModel {
        val shopLayoutV2Data = member.asJsonObject
        val dynamicTabMockResponseData = generateMockDynamicTabData(shopLayoutV2Data)
        return ShopPageMockWidgetModel(
            Pair(
                dynamicTabMockResponseData.toString(),
                shopLayoutV2Data.toString()
            )
        )
    }

    fun generateMockDynamicTabData(jsonObjectData: JsonObject?): JsonObject {
        val widgetId = jsonObjectData?.get("widgetID")?.asBigInteger.orZero()
        val widgetMasterId = jsonObjectData?.get("widgetMasterID")?.asBigInteger.orZero()
        val widgetType = jsonObjectData?.get("type")?.asString
        val widgetName = jsonObjectData?.get("name")?.asString
        return JsonObject().apply {
            addProperty("widgetID", widgetId)
            addProperty("widgetMasterID", widgetMasterId)
            addProperty("widgetType", widgetType)
            addProperty("widgetName", widgetName)
        }
    }

    fun generateMockBmsmWidgetData(
        resources: Resources,
        rawResourcePath: Int
    ): BmsmMockWidgetModel {
        val offeringInfo = getMockWidgetJsonFromRaw(resources, rawResourcePath)
        val offeringProduct = getMockWidgetJsonFromRaw(resources, BMSM_WIDGET_OFFERING_PRODUCT_DATA_SOURCE)
        val offeringInfoResponseData = offeringInfo?.first()?.asJsonObject.toString()
        val offeringProductResponseData = offeringProduct?.first()?.asJsonObject.toString()
        return BmsmMockWidgetModel(Pair(offeringInfoResponseData, offeringProductResponseData))
    }

    private fun getMockWidgetJsonFromRaw(resources: Resources, rawResourcePath: Int): JsonArray? {
        val rawResource: InputStream = resources.openRawResource(rawResourcePath)
        val content: String = GraphqlHelper.streamToString(rawResource)
        try {
            rawResource.close()
        } catch (e: IOException) {
            Timber.e(e)
        }
        return JsonParser.parseString(content).asJsonArray
    }

    fun getStringMockWidgetJsonFromRaw(resources: Resources, rawResourcePath: Int): String {
        val rawResource: InputStream = resources.openRawResource(rawResourcePath)
        val content: String = GraphqlHelper.streamToString(rawResource)
        try {
            rawResource.close()
        } catch (e: IOException) {
            Timber.e(e)
        }
        val jsonArray = JsonParser.parseString(content).asJsonArray
        return jsonArray?.first()?.asJsonObject.toString()
    }

    fun updateWidgetId(mockShopWidgetData: List<ShopPageMockWidgetModel>) {
        mockShopWidgetData.mapIndexed { index, element ->
            element.editWidgetId(index)
        }
    }

    fun mapToShopPageMockWidgetModel(serializedList: String): List<ShopPageMockWidgetModel> {
        val type = object : TypeToken<List<Pair<String, String>>>() {}.type
        val listPairMockShopData = (gson.fromJson(serializedList, type) as List<Pair<String, String>>)
        return listPairMockShopData.map {
            ShopPageMockWidgetModel(it)
        }
    }

    fun listMockWidgetDataToJson(listMockWidgetData: List<Pair<String, String>>): String? {
        return gson.toJson(listMockWidgetData)
    }

    fun bmsmWidgetMockDataToJson(widgetData: Pair<String, String>): String? {
        return gson.toJson(widgetData)
    }
}
