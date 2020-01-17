package com.tokopedia.sellerhome.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 2020-01-17
 */

data class TickerResponse(
        @SerializedName("data")
        @Expose
        val data: Data? = null,

        @SerializedName("meta")
        @Expose
        val meta: Meta? = null
)

data class Data(
        @SerializedName("tickers")
        @Expose
        val tickers: List<TickerModel>?
)

data class Meta(
        @SerializedName("total_data")
        @Expose
        val totalData: String?
)

data class TickerModel(
        @SerializedName("redirect_url")
        @Expose
        val redirectUrl: String?,

        @SerializedName("created_by")
        @Expose
        val createdBy: String?,

        @SerializedName("created_on")
        @Expose
        val createdOn: String?,

        @SerializedName("state")
        @Expose
        val state: String?,

        @SerializedName("expire_time")
        @Expose
        val expireTime: String?,

        @SerializedName("id")
        @Expose
        val id: String?,

        @SerializedName("message")
        @Expose
        val message: String?,

        @SerializedName("title")
        @Expose
        val title: String?,

        @SerializedName("target")
        @Expose
        val target: String?,

        @SerializedName("device")
        @Expose
        val device: String?,

        @SerializedName("updated_on")
        @Expose
        val updatedOn: String?,

        @SerializedName("updated_by")
        @Expose
        val updatedBy: String?,

        @SerializedName("color")
        @Expose
        val color: String?
)