package com.tokopedia.checkout.old.domain.model.checkout

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageData(
        var title: String = "",
        var desc: String = "",
        var action: String = ""
) : Parcelable