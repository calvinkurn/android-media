package com.tokopedia.checkout.bundle.domain.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageData(
        var title: String = "",
        var desc: String = "",
        var action: String = ""
) : Parcelable