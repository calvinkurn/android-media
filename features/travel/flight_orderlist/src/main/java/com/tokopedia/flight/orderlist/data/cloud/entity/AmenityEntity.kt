package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by alvarisi on 12/6/17.
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
        val isWifi: Boolean = false)
