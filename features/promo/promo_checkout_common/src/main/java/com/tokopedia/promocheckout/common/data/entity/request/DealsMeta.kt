package com.tokopedia.promocheckout.common.data.entity.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DealsMeta(@SerializedName("title")
                @Expose
                val title: String = "",
                @SerializedName("webURL")
                @Expose
                val webUrl: String = "",
                @SerializedName("appURL")
                @Expose
                val appUrl: String = "",
                @SerializedName("type")
                @Expose
                val type: String = "")