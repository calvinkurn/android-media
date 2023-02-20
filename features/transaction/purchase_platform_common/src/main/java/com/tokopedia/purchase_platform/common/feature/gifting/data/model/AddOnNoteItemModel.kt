package com.tokopedia.purchase_platform.common.feature.gifting.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnNoteItemModel(
    @SerializedName("is_custom_note")
    var isCustomNote: Boolean = false,
    @SerializedName("to")
    var to: String = "",
    @SerializedName("from")
    var from: String = "",
    @SerializedName("notes")
    var notes: String = "",
) : Parcelable
