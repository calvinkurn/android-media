package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.ComponentNames


enum class ComponentsList(val id: Int, val componentName: String) {
    Video(R.layout.multi_banner_layout, "video"),
    SingleBanner(R.layout.multi_banner_layout, "banner_image"),
    DoubleBanner(R.layout.multi_banner_layout, "banner_image_double"),
    TripleBanner(R.layout.multi_banner_layout, "banner_image_triple"),
    QuadrupleBanner(R.layout.multi_banner_layout, "banner_image_quadruple"),
    Share(R.layout.multi_banner_layout, "share"),
    BannerTimer(R.layout.banner_timer_layout, "timer_with_banner"),
    Tokopoints(R.layout.tokopoints_layout, "tokopoints"),
    TokopointsItem(R.layout.tokopoints_item_layout, "tokopoints_item"),
    CpmTopAds(R.layout.cpm_topads_layout, "topads"),
    CpmTopAdsItem(R.layout.item_cpm_topads_shop_layout, ComponentNames.CpmTopAdsShopItem.componentName),
    CpmTopAdsProductItem(R.layout.item_cpm_topads_product_layout, ComponentNames.CpmTopAdsProductItem.componentName),
}
