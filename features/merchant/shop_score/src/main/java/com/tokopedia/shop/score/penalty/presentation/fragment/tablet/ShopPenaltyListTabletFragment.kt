package com.tokopedia.shop.score.penalty.presentation.fragment.tablet

import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageFragment
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.sortfilter.SortFilterItem

class ShopPenaltyListTabletFragment: ShopPenaltyPageFragment() {

    override fun onItemPenaltyClick(itemPenaltyUiModel: ItemPenaltyUiModel) {
        context?.let {
            if (DeviceScreenInfo.isTablet(it)) {

            } else {
                super.onItemPenaltyClick(itemPenaltyUiModel)
            }
        } ?: super.onItemPenaltyClick(itemPenaltyUiModel)
    }

    override fun onDateClick() {
        super.onDateClick()
    }

    override fun onClickFilterApplied(penaltyFilterUiModelList: List<PenaltyFilterUiModel>) {
        super.onClickFilterApplied(penaltyFilterUiModelList)
    }

    override fun onChildSortFilterItemClick(sortFilterItem: SortFilterItem) {
        super.onChildSortFilterItemClick(sortFilterItem)
    }

    interface SomListClickListener {
        fun closeOrderDetail()
    }
}