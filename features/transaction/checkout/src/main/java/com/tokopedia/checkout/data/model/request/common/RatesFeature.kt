package com.tokopedia.checkout.data.model.request.common

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RatesFeature(
    @SerializedName("ontime_delivery_guarantee")
    var ontimeDeliveryGuarantee: OntimeDeliveryGuarantee? = null
): Parcelable