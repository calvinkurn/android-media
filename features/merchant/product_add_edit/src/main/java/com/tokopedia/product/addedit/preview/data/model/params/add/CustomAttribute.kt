package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomAttribute(
    val key: String = "",
    val value: String = "",
    val source: String = "",
): Parcelable
