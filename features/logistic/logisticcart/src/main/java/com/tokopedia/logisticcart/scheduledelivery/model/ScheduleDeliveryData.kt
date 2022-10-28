package com.tokopedia.logisticcart.scheduledelivery.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.AdditionalDeliveryData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.RatesValidation

data class ScheduleDeliveryData(
    @SerializedName("rates_validation")
    val ratesValidation: RatesValidation = RatesValidation(),
    @SerializedName("additional_delivery_data")
    val additionalDeliveryData: AdditionalDeliveryData = AdditionalDeliveryData(),
)
