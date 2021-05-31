package com.tokopedia.purchase_platform.common.feature.tickerannouncement

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TickerData(
        var id: String = "",
        var message: String = "",
        var page: String = "",
        var title: String = ""
) : Parcelable {

    fun isValid(page: String?): Boolean {
        return (page == null || page.equals(this.page, true)) && message.isNotBlank()
    }
}