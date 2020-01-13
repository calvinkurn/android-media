package com.tokopedia.home_page_banner.presenter.handler

interface HomePageBannerListener {
    fun onPromoAllClick()
    fun onPromoClick(position: Int)
    fun onPromoScrolled(position: Int)
}