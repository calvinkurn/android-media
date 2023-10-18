package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyPointCardUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.sortfilter.SortFilterItem

interface ItemDetailPenaltyListener {
    fun onItemPenaltyClick(itemPenaltyUiModel: ItemPenaltyUiModel)
}

interface FilterPenaltyBottomSheetListener {
    fun onChipsFilterItemClick(
        nameFilter: String,
        chipType: String,
        chipTitle: String,
        position: Int
    )

    fun onSeeAllButtonClicked(
        uiModel: PenaltyFilterUiModel
    )
}

interface FilterPenaltyDateListener {
    fun onDatePicked(
        startDate: String,
        defaultStartDate: String,
        endDate: String,
        defaultEndDate: String
    )
}

interface FilterPenaltyTypesBottomSheetListener {
    fun onFilterSaved(filterList: List<Int>)
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

interface ItemPenaltyInfoNotificationListener {
    fun onNotYetPenaltyCardClicked(latestOngoingPenaltyId: String?)
}

interface ItemSortFilterPenaltyListener {
    fun onParentSortFilterClicked()
    fun onChildSortFilterItemClick(sortFilterItem: SortFilterItem)
}

interface ItemPenaltyTickerListener {
    fun onDescriptionClicked(linkUrl: String)
}

interface ItemPenaltySubsectionListener {
    fun onPenaltySubsectionIconClicked()
}

interface ItemPenaltyPointCardListener {
    fun onPenaltyPointsButtonClicked(uiModel: ItemPenaltyPointCardUiModel)
}
