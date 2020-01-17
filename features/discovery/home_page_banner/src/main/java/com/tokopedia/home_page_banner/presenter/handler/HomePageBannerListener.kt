package com.tokopedia.home_page_banner.presenter.handler

interface HomePageBannerListener {
    fun onPageDragStateChanged(isDrag: Boolean)
    fun onPromoAllClick()
    fun onPromoClick(position: Int)
    fun onPromoScrolled(position: Int)
}