package com.tokopedia.home.beranda.domain.model.salam_widget

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by firman on 09-06-2020
 */

data class SalamWidgetData(
    @Expose
    @SerializedName("AppLink")
    val appLink: String = "",
    @Expose
    @SerializedName("BackgroundColor")
    val backgroundColor: String = "",
    @Expose
    @SerializedName("ButtonText")
    val buttonText: String = "",
    @Expose
    @SerializedName("ID")
    val id: Int = 0,
    @Expose
    @SerializedName("IconURL")
    val iconURL: String = "",
    @Expose
    @SerializedName("Link")
    val link: String = "",
    @Expose
    @SerializedName("mainText")
    val mainText: String = "",
    @Expose
    @SerializedName("SubText")
    val subText: String = "",
    @Expose
    @SerializedName("Title")
    val title: String = ""
)