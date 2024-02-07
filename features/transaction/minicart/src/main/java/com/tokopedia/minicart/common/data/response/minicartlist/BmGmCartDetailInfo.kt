package com.tokopedia.minicart.common.data.response.minicartlist

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.cartcommon.data.response.bmgm.BmGmData
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
