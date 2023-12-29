package com.tokopedia.shop.home.view.model.showcase_navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Showcase(
    val id: String,
    val name: String,
    val imageUrl: String,
    val ctaLink: String,
    val isMainBanner: Boolean
) : Parcelable
