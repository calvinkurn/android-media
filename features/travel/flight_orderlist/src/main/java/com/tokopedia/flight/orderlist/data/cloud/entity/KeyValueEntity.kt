package com.tokopedia.flight.orderlist.data.cloud.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 26/08/2019
 */
@Parcelize
class KeyValueEntity(@SerializedName("key")
                     @Expose
                     val key: String = "",
                     @SerializedName("value")
                     @Expose
                     val value: String = "") : Parcelable