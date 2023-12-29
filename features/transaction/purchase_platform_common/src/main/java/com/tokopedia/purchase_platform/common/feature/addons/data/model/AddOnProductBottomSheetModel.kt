package com.tokopedia.purchase_platform.common.feature.addons.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnProductBottomSheetModel(
    var title: String = "",
    var applink: String = "",
    var isShown: Boolean = false
) : Parcelable
