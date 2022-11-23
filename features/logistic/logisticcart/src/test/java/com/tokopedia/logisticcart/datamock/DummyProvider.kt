package com.tokopedia.logisticcart.datamock

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.logisticcart.FileUtils
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.EstimatedTimeArrivalPromo
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesGqlResponse
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter

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

    fun getRatesResponseWithPromo() : RatesGqlResponse = Gson().fromJson(
        fileUtils.getJsonFromAsset("rates_with_promo_response.json"),
        RatesGqlResponse::class.java
    )

    fun getShippingDataWithServiceUiRatesHidden() : ShippingRecommendationData {
        val ratesData = getRatesResponseWithPromo()
        val shippingRecomData = ShippingDurationConverter().convertModel(ratesData.ratesData)
        val hiddenService = shippingRecomData.shippingDurationUiModels.first()
        hiddenService.serviceData.isUiRatesHidden = true
        return shippingRecomData
    }

    fun getShippingDataWithoutEligibleCourierPromo() : ShippingRecommendationData {
        val ratesData = getRatesResponseWithPromo()
        val shippingRecomData = ShippingDurationConverter().convertModel(ratesData.ratesData)
        val durationUiModels = shippingRecomData.shippingDurationUiModels.toMutableList()
        durationUiModels.removeAt(1)
        shippingRecomData.shippingDurationUiModels = durationUiModels
        shippingRecomData.listLogisticPromo = listOf()
        return shippingRecomData
    }

    fun getShippingDataWithPromoAndPreOrderModel() : ShippingRecommendationData {
        val ratesData = getRatesResponseWithPromo()
        val shippingRecomData = ShippingDurationConverter().convertModel(ratesData.ratesData)
        shippingRecomData.preOrderModel = PreOrderModel("header", "label", true)
        return shippingRecomData
    }

    fun getShippingDataWithServiceError(errorCode: Int = 1) : ShippingRecommendationData {
        val ratesData = getRatesResponseWithPromo()
        val shippingRecomData = ShippingDurationConverter().convertModel(ratesData.ratesData)
        val errorService = shippingRecomData.shippingDurationUiModels.first()
        errorService.etaErrorCode = errorCode
        return shippingRecomData
    }

    fun getShippingDataWithPromoEtaError() : ShippingRecommendationData {
        val ratesData = getRatesResponseWithPromo()
        val shippingRecomData = ShippingDurationConverter().convertModel(ratesData.ratesData)
        val errorEtaPromo = shippingRecomData.listLogisticPromo.first().copy(etaData = EstimatedTimeArrivalPromo(textEta = "", errorCode = 1))
        shippingRecomData.listLogisticPromo = listOf(errorEtaPromo)
        return shippingRecomData
    }

    fun getShippingDataWithPromoEtaErrorAndTextEta() : ShippingRecommendationData {
        val ratesData = getRatesResponseWithPromo()
        val shippingRecomData = ShippingDurationConverter().convertModel(ratesData.ratesData)
        val errorEtaPromo = shippingRecomData.listLogisticPromo.first().copy(etaData = EstimatedTimeArrivalPromo(textEta = "", errorCode = -1))
        shippingRecomData.listLogisticPromo = listOf(errorEtaPromo)
        return shippingRecomData
    }

}
