package com.tokopedia.deals.ui.home.listener

import com.tokopedia.deals.ui.home.ui.dataview.DealsCategoryDataView

interface DealsCategoryListener {
    fun onDealsCategoryClicked(dealsCategory: DealsCategoryDataView, position: Int)
    fun onDealsCategorySeeAllClicked(categories: List<DealsCategoryDataView>)
}
