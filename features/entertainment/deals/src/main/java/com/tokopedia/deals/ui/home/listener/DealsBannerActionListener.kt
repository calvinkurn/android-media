package com.tokopedia.deals.ui.home.listener

import com.tokopedia.deals.ui.home.ui.dataview.BannersDataView

/**
 * @author by jessica on 16/06/20
 */

interface DealsBannerActionListener {
    fun onBannerScroll(banner: BannersDataView.BannerDataView, position: Int)
    fun onBannerClicked(banner: List<BannersDataView.BannerDataView>, position: Int)
    fun onBannerSeeAllClick(bannerSeeAllUrl: String)
}
