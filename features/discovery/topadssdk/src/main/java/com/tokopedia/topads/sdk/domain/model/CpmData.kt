package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CpmData(
    @SerializedName("id")
    var id: String = "",

    @SerializedName("ad_ref_key")
    var adRefKey: String = "",

    @SerializedName("redirect")
    var redirect: String = "",

    @SerializedName("ad_click_url")
    var adClickUrl: String = "",

    @SerializedName("headline")
    var cpm: Cpm = Cpm(),

    @SerializedName("applinks")
    var applinks: String = ""
) : Parcelable
