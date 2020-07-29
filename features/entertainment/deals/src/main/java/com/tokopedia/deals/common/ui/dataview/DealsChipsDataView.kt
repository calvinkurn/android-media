package com.tokopedia.deals.common.ui.dataview

data class DealsChipsDataView(
    val chipList: List<ChipDataView> = emptyList(),
    val showingLimit: Int = 0
) : DealsBaseItemDataView()