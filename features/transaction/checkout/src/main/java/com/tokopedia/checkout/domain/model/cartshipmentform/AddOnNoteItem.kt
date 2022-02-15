package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnNoteItem(
        var isCustomNote: Boolean = false,
        var to: String = "",
        var from: String = "",
        var notes: String = ""
) : Parcelable
