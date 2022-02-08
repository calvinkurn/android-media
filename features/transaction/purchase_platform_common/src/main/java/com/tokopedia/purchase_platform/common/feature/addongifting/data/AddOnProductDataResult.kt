package com.tokopedia.purchase_platform.common.feature.addongifting.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnSavedStateResult(
        var addOns: List<AddOnSavedState> = emptyList()
) : Parcelable

@Parcelize
data class AddOnSavedState(
        var addOnKey: String = "",
        var addOnLevel: String = "",
        var addOnData: List<AddOnData> = emptyList()
) : Parcelable

@Parcelize
data class AddOnData(
        var addOnId: String = "",
        var addOnQty: Int = 0,
        var addOnPrice: Double = 0.0,
        var addOnMetadata: AddOnMetadata = AddOnMetadata()
) : Parcelable

@Parcelize
data class AddOnMetadata(
        var addOnNote: AddOnNote = AddOnNote()
) : Parcelable

@Parcelize
data class AddOnNote(
        var isCustomNote: Boolean = false,
        var to: String = "",
        var from: String = "",
        var notes: String = ""
) : Parcelable