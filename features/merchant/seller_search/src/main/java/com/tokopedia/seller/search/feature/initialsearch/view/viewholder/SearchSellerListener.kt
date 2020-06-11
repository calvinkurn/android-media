package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

interface FilterSearchListener {
    fun onFilterItemClicked(title: String, chipType: String, position: Int)
}