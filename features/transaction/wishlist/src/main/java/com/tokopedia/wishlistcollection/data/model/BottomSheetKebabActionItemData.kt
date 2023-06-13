package com.tokopedia.wishlistcollection.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BottomSheetKebabActionItemData(
    val text: String = "",
    val action: String = "",
    val url: String = ""
) : Parcelable
