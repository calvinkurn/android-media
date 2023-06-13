package com.tokopedia.purchase_platform.common.feature.gifting.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnMetadataItemModel(
    @SerializedName("add_on_note")
    var addOnNoteItemModel: AddOnNoteItemModel = AddOnNoteItemModel()
) : Parcelable
