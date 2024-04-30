package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DisabledFeatures(
    var isNoteEditDisabledFeatures: Boolean = false,
    var isQtyEditDisabledFeatures: Boolean = false
) : Parcelable {

    companion object {
        const val QTY_EDIT = "QTY_EDITOR"
        const val NOTE_EDIT = "NOTE_EDITOR"
    }
}
