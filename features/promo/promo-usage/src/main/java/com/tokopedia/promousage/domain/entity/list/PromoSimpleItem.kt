package com.tokopedia.promousage.domain.entity.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromoSimpleItem(
    val title: String,
    val type: String,
    val typeColor: String,
    val desc: String,
    val backgroundUrl: String,
    val iconUrl: String
): Parcelable
