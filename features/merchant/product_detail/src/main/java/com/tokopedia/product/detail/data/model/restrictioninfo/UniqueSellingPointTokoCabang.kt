package com.tokopedia.product.detail.data.model.restrictioninfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 26/02/21
 */
data class UniqueSellingPointTokoCabang(
        @SerializedName("bebasOngkirExtra")
        @Expose
        val uspBoe: UniqueSellingPointBoe = UniqueSellingPointBoe()
)

data class UniqueSellingPointBoe(
        @SerializedName("icon")
        @Expose
        val uspIcon: String = "" // only for static image purpose
)