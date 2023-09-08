package com.tokopedia.cartrevamp.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartNoteBottomSheetData(
    val productName: String = "",
    val productImage: String = "",
    val variant: String = "",
    val note: String = ""
) : Parcelable
