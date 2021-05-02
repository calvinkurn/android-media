package com.tokopedia.topads.edit.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KeySharedModel (
        var name : String ="",
        val totalSearch: String = "baru",
        val competition : String = "baru",
        var priceBid:String = "0",
        val minBudget:String = "0",
        val source:String = "",
        var typeInt:Int = 11,
        var id:String = "",
):Parcelable