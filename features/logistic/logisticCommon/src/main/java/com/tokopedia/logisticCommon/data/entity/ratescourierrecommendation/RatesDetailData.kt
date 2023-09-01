package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

data class RatesDetailData(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("rates_id")
    val ratesId: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("origin")
    val originData: OriginData = OriginData(),
    @SerializedName("destination")
    val destinationData: DestinationData = DestinationData(),
    @SerializedName("weight")
    val weight: String = "",
    @SerializedName("services")
    val services: List<ServiceData> = listOf(),
    @SerializedName("info")
    val info: InfoRatesDetailData = InfoRatesDetailData(),
    @SerializedName("error")
    val error: ErrorRatesDetailData = ErrorRatesDetailData(),
    @SerializedName("promo_stackings")
    val listPromoStacking: List<PromoStacking> = listOf(),
    @SerializedName("pre_order")
    val preOrder: PreOrder = PreOrder()
)
