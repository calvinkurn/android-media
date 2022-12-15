package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class PollingOption(
    @SerializedName("option_id")
    @Expose val optionId: String = "",

    @SerializedName("option")
    @Expose val option: String = "",

    @SerializedName("voter")
    @Expose val voter: Int = 0,

    @SerializedName("percentage")
    @Expose val percentage: Int = 0,

    @SerializedName("weblink")
    @Expose val weblink: String = "",

    @SerializedName("applink")
    @Expose val applink: String = "",

    @SerializedName("is_selected")
    @Expose val isSelected: Boolean = false,

    @SerializedName("image_option")
    @Expose val imageOption: String = ""
)
