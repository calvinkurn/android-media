package com.tokopedia.logisticcart.datamock

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.FileUtils
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import java.io.File

internal object DummyProvider {

    val fileUtils: FileUtils by lazy { FileUtils() }

    fun getShippingParam(): ShippingParam =
            Gson().fromJson(shippingParamJson, ShippingParam::class.java)

    fun getShopShipments(): List<ShopShipment> =
            Gson().fromJson(shopShipmentsJson, object : TypeToken<List<ShopShipment>>() {}.type)

    val shippingRecommendationDataNoState: ShippingRecommendationData =
            Gson().fromJson(fileUtils.getJsonFromAsset("rates.json"), ShippingRecommendationData::class.java)

    val shippingRecommendationDataWithState: ShippingRecommendationData =
            Gson().fromJson(fileUtils.getJsonFromAsset("ratesWithState.json"), ShippingRecommendationData::class.java)

}