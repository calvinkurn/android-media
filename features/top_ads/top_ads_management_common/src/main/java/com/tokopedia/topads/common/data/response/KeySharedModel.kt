package com.tokopedia.topads.common.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KeySharedModel (
        var name : String ="",
        val totalSearch: String = "-",
        val competition : String = "-",
        var priceBid:String = "0",
        val minBudget:String = "0",
        val source:String = "",
        var typeInt:Int = 11,
        var id:String = "",
):Parcelable
