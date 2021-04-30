package com.tokopedia.topads.edit.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KeySharedModel (
        val name : String ="",
        val totalSearch: String = "baru",
        val competition : String = "baru",
        var priceBid:String = "0",
        val minBudget:String = "0",
        val source:String = "",
        val type:String = "",
        val typeInt:Int = 11,
        val status:Int = 1,
        val id:String = "",
        val suggestedBid:String = "0"
):Parcelable