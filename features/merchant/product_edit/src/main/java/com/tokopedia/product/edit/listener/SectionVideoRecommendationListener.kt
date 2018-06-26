package com.tokopedia.product.edit.listener


interface SectionVideoRecommendationListener {

    fun onShowMoreClicked()

    fun onVideoRecommendationFeaturedClicked(videoIDs: ArrayList<String>)

    fun setProductAddVideoListener(listener: ProductAddVideoListener)
}