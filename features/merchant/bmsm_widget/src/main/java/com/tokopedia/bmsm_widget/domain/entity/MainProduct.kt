package com.tokopedia.bmsm_widget.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainProduct(
    val productId: Long,
    val quantity: Int
) : Parcelable
