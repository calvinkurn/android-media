package com.tokopedia.logisticcart.datamock

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.logisticcart.FileUtils
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel

internal object DummyProvider {

    val fileUtils: FileUtils by lazy { FileUtils() }

    fun getShippingParam(): ShippingParam =
            Gson().fromJson(shippingParamJson, ShippingParam::class.java)

    fun getShopShipments(): List<ShopShipment> =
            Gson().fromJson(shopShipmentsJson, object : TypeToken<List<ShopShipment>>() {}.type)

    fun getProducts(): List<Product> =
            Gson().fromJson(productsJson, object : TypeToken<List<Product>>() {}.type)

    fun getShippingRecommendationDataNoState(): ShippingRecommendationData =
            Gson().fromJson(
                    fileUtils.getJsonFromAsset("rates.json"),
                    ShippingRecommendationData::class.java)

    fun getShippingRecommendationDataWithState(): ShippingRecommendationData =
            Gson().fromJson(
                    fileUtils.getJsonFromAsset("ratesWithState.json"),
                    ShippingRecommendationData::class.java)

    fun getShipmentDetailData(): ShipmentDetailData =
            Gson().fromJson(
                    fileUtils.getJsonFromAsset("shipment_detail_data.json"),
                    ShipmentDetailData::class.java
            )

    fun getAddress(): RecipientAddressModel =
            Gson().fromJson(
                    fileUtils.getJsonFromAsset("address.json"),
                    RecipientAddressModel::class.java
            )

}