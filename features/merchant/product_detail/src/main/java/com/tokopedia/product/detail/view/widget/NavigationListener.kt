package com.tokopedia.product.detail.view.widget

interface NavigationListener {
    fun onImpressionNavigationTab(labels: List<String>)
    fun onImpressionBackToTop(label: String)
    fun onClickNavigationTab(position: Int, label: String)
    fun onClickBackToTop(position: Int, label: String)
}