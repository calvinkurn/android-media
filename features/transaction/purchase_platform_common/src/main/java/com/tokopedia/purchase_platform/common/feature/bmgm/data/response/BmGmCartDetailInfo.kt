package com.tokopedia.purchase_platform.common.feature.bmgm.data.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BmGmCartDetailInfo(
    @Expose
    @SerializedName("cart_detail_type")
    val cartDetailType: String = "",
    @Expose
    @SerializedName("bmgm")
    val bmgmData: BmGmData = BmGmData()
) : Parcelable
