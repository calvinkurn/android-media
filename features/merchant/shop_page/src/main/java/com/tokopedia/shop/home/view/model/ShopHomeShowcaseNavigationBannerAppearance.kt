package com.tokopedia.shop.home.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ShopHomeShowcaseNavigationBannerAppearance {
    val viewAllCtaAppLink: String
    val title: String
}

data class Carousel(
    override val title: String,
    val showcases: List<Showcase>,
    override val viewAllCtaAppLink: String
) : ShopHomeShowcaseNavigationBannerAppearance

data class TopMainBanner(
    override val title: String,
    val showcases: List<Showcase>,
    override val viewAllCtaAppLink: String,
) : ShopHomeShowcaseNavigationBannerAppearance

data class LeftMainBanner(
    val tabs: List<Tab>,
    override val title: String,
    override val viewAllCtaAppLink: String,
) : ShopHomeShowcaseNavigationBannerAppearance

data class Tab(
    val text: String,
    val imageUrl: String,
    val showcases: List<Showcase>
)


@Parcelize
data class Showcase(
    val id: String,
    val name: String,
    val imageUrl: String,
    val ctaLink: String,
    val isMainBanner: Boolean
) : Parcelable
