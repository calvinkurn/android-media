package com.tokopedia.topads.edit.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KeySharedModel (
        var name : String ="",
        val totalSearch: String = "-",
        val competition : String = "-",
        var priceBid:Float = 0.0f,
        val minBudget:Float = 0.0f,
        val source:String = "",
        var typeInt:Int = 11,
        var id:String = "",
):Parcelable