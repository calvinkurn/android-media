package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnMetadataItemModel(
        var addOnNoteItemModel: AddOnNoteItemModel = AddOnNoteItemModel()
): Parcelable
