package com.tokopedia.checkout.data.model.request.checkout.cross_sell

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrossSellRequest(
        @SerializedName("main_vertical_id")
        var mainVerticalId: Int = 1,
        @SerializedName("items")
        var listItem: ArrayList<CrossSellItemRequestModel> = arrayListOf()
) : Parcelable
