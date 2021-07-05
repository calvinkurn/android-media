package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailErrorModel(
        @SerializedName("ID")
        @Expose
        val id: String = "",
        @SerializedName("Status")
        @Expose
        val status: String = "",
        @SerializedName("Title")
        @Expose
        val title: String = "",
        @SerializedName("Message")
        @Expose
        val message: String = ""
) : Parcelable