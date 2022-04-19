package com.tokopedia.flight.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 09/06/2021
 */
data class FlightError(
        @SerializedName("id")
        @Expose
        var id: String = "",
        @SerializedName("status")
        @Expose
        var status: String = "",
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("head")
        @Expose
        var head: String = "",
        @SerializedName("message")
        @Expose
        var message: String = ""
) {

    override fun equals(other: Any?): Boolean =
            other is FlightError &&
                    other.id.equals(id, true)

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = title

}