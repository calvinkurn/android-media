package com.tokopedia.wallet.ovoactivation.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 01/10/18.
 */
class PhoneActionEntity (
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("text")
    @Expose
    val text: String = "",
    @SerializedName("applink")
    @Expose
    val applink: String = "")
