package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StatusFilteredData (
        @SerializedName("tier")
        @Expose
        val tier: TierData = TierData()
)

data class TierData (
        @SerializedName("nameDesc")
        @Expose
        val nameDesc: String = "",
        @SerializedName("eggImageHomepageURL")
        @Expose
        val imageURL: String = "",
        @SerializedName("backgroundImgURLMobile")
        @Expose
        val backgroundImageURL: String = ""
)