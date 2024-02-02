package com.tokopedia.deals.ui.home.ui.dataview

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DealsCategoryDataView(
        val id: String = "",
        val imageUrl: String = "",
        val title: String = "",
        val appUrl: String = ""): Parcelable
