package com.tokopedia.checkout.old.data.model.request.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OntimeDeliveryGuarantee(
        @SerializedName("available")
        var available: Boolean = false,
        @SerializedName("duration")
        var duration: Int = 0
) : Parcelable