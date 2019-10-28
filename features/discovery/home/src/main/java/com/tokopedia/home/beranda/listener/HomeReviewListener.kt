package com.tokopedia.home.beranda.listener

interface HomeReviewListener {
    fun onReviewClick(position: Int, clickReviewAt: Int)
    fun onCloseClick(position: Int)
    fun getReviewData(adapterPosition: Int)
}