package com.tokopedia.deals.home.listener

import android.view.View
import com.tokopedia.deals.home.ui.dataview.DealsCategoryDataView

interface DealsCategoryListener {
    fun onDealsCategoryClicked(dealsCategory: DealsCategoryDataView, position: Int)
    fun onDealsCategorySeeAllClicked(categories: List<DealsCategoryDataView>)
}