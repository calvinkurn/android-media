package com.tokopedia.cartrevamp.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartMultipleBOBottomSheetData(
    val type: String = "",
    val title: String = "",
    val description: String = "",
    val buttonText: String = "",
    val imageUrl: String = ""
) : Parcelable
