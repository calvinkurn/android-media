package com.tokopedia.mvc.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopShowcase(
    val id: Long,
    val alias: String,
    val name: String,
    val type: Int,
    val isSelected: Boolean
) : Parcelable
