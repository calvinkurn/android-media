package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnMetadataItem(
        var addOnNoteItem: AddOnNoteItem = AddOnNoteItem(),
) : Parcelable
