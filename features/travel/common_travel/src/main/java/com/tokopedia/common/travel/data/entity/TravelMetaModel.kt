package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 06/08/2019
 */
class TravelMetaModel(@SerializedName("title")
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
