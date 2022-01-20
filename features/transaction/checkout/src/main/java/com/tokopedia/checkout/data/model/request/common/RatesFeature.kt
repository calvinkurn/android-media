package com.tokopedia.checkout.data.model.request.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RatesFeature(
        @SerializedName("ontime_delivery_guarantee")
        var ontimeDeliveryGuarantee: OntimeDeliveryGuarantee? = null
) : Parcelable