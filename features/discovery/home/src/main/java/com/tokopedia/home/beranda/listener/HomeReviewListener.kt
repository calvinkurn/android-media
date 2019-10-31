package com.tokopedia.home.beranda.listener

interface HomeReviewListener {
    fun onReviewClick(position: Int, clickReviewAt: Int, applink:String)
    fun onCloseClick()
}