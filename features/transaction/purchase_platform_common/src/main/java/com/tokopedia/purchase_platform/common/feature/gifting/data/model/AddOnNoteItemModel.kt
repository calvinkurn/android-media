package com.tokopedia.purchase_platform.common.feature.gifting.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnNoteItemModel(
        var isCustomNote: Boolean = false,
        var to: String = "",
        var from: String = "",
        var notes: String = "",
): Parcelable
