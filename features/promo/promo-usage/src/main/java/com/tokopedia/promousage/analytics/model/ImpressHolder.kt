package com.tokopedia.promousage.analytics.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class ImpressHolder(
    open var isInvoke: Boolean = false
) : Parcelable {

    operator fun invoke() {
        isInvoke = true
    }
}
