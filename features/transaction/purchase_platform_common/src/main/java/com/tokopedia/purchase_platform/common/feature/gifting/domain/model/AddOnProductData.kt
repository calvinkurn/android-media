package com.tokopedia.purchase_platform.common.feature.gifting.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnData(
        var addOnId: String = "",
        var addOnMetadata: AddOnMetadata = AddOnMetadata(),
        var addOnPrice: Int = 0,
        var addOnQty: Int = 0
) : Parcelable

@Parcelize
data class AddOnMetadata(
        var addOnNote: AddOnNote = AddOnNote()
) : Parcelable

@Parcelize
data class AddOnNote(
        var from: String = "",
        var isCustomNote: Boolean = false,
        var notes: String = "",
        var to: String = ""
) : Parcelable
