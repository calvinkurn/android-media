package com.tokopedia.vouchercreation.product.list.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShowCaseSelection(
        val id: String = "",
        val name: String = "",
        var isSelected: Boolean = false
) : Parcelable