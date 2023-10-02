package com.tokopedia.promousage.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromoItemClashingInfo(
    val code: String = "",
    val message: String = ""
) : Parcelable
