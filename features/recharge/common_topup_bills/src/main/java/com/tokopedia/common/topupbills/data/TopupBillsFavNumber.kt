package com.tokopedia.common.topupbills.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TopupBillsFavNumber(
        @SerializedName("list")
        @Expose
        val favNumberList: List<TopupBillsFavNumberItem>) : Parcelable