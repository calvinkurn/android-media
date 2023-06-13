package com.tokopedia.logisticcart.scheduledelivery.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.OntimeDeliveryGuarantee
import kotlinx.parcelize.Parcelize

@Parcelize
class Features(
    @SerializedName("ontime_delivery_guarantee")
    val onTimeDeliveryGuarantee: OntimeDeliveryGuarantee = OntimeDeliveryGuarantee()
) : Parcelable
