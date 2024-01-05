package com.tokopedia.deals.ui.category.data

import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.ui.search.model.response.CuratedData

data class DealsCategoryCombined(
    val category : CuratedData = CuratedData(),
    val brandProduct: SearchData = SearchData()
)
