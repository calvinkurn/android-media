package com.tokopedia.vouchercreation.product.list.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SortSelection(
        val id: String = "",
        val value: String = "",
        val name: String = "",
        var isSelected: Boolean = false
) : Parcelable