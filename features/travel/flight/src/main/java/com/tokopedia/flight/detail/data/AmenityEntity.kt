package com.tokopedia.flight.detail.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 1/17/22.
 */

class AmenityEntity(
    @SerializedName("cabin_baggage")
    @Expose
    val cabinBaggage: BaggageEntity = BaggageEntity(),
    @SerializedName("free_baggage")
    @Expose
    val freeBaggage: BaggageEntity = BaggageEntity(),
    @SerializedName("meal")
    @Expose
    val isMeal: Boolean = false,
    @SerializedName("usb_port")
    @Expose
    val isUsbPort: Boolean = false,
    @SerializedName("wifi")
    @Expose
    val isWifi: Boolean = false
)

/**
 * @author by furqan on 1/17/22.
 */

class BaggageEntity(
    @SerializedName("is_up_to")
    @Expose
    var isUpTo: Boolean = false,
    @SerializedName("unit")
    @Expose
    var unit: String = "",
    @SerializedName("value")
    @Expose
    var value: String = ""
)

@Parcelize
data class Amenity(
    @SerializedName("icon")
    @Expose
    var icon: String = "",
    @SerializedName("label")
    @Expose
    var label: String = ""
) : Parcelable {
    var isDefault = false
}

