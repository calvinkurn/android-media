package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.sortfilter.SortFilterItem

interface ItemDetailPenaltyListener {
    fun onItemPenaltyClick(itemPenaltyUiModel: ItemPenaltyUiModel)
}

interface FilterPenaltyBottomSheetListener {
    fun onChipsFilterItemClick(nameFilter: String, chipType: String, chipTitle: String, position: Int)
}

interface ItemHeaderCardPenaltyListener {
    fun impressLearnMorePenaltyPage()
    fun onMoreInfoHelpPenaltyClicked()
}

interface ItemPeriodDateFilterListener {
    fun onDateClick()
}

interface ItemPenaltyErrorListener {
    fun onRetryRefreshError()
}

interface ItemSortFilterPenaltyListener {
    fun onParentSortFilterClicked()
    fun onChildSortFilterItemClick(sortFilterItem: SortFilterItem)
}