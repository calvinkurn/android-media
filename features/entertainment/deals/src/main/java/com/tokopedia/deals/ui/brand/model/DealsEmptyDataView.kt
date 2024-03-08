package com.tokopedia.deals.ui.brand.model

import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView

data class DealsEmptyDataView (
        var title: String = "",
        val desc: String = "",
        val isFilter: Boolean = false
): DealsBaseItemDataView()
