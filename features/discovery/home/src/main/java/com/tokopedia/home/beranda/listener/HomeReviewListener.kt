package com.tokopedia.home.beranda.listener

interface HomeReviewListener {
    fun onReviewClick(position: Int, clickReviewAt: Int, delay: Long, applink:String)
    fun onCloseClick()
}