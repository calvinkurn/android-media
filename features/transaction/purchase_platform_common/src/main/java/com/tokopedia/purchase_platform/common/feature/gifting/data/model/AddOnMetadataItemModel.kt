package com.tokopedia.purchase_platform.common.feature.gifting.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnMetadataItemModel(
        var addOnNoteItemModel: AddOnNoteItemModel = AddOnNoteItemModel()
): Parcelable
