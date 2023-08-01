package com.tokopedia.cartrevamp.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartBundlingBottomSheetData(
    val title: String = "",
    val description: String = "",
    val bottomTicker: String = "",
    val bundleIds: List<String> = emptyList()
) : Parcelable
