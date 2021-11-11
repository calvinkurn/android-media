package com.tokopedia.shop.score.penalty.presentation.fragment.tablet

import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageFragment
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.sortfilter.SortFilterItem

class ShopPenaltyListTabletFragment : ShopPenaltyPageFragment() {

    private var penaltyListListener: PenaltyListListener? = null

    override fun onItemPenaltyClick(itemPenaltyUiModel: ItemPenaltyUiModel) {
        context?.let {
            if (DeviceScreenInfo.isTablet(it)) {
                penaltyListListener?.onItemPenaltyClicked(itemPenaltyUiModel)
            } else {
                super.onItemPenaltyClick(itemPenaltyUiModel)
            }
        } ?: super.onItemPenaltyClick(itemPenaltyUiModel)
    }

    override fun onDateClick() {
        penaltyListListener?.closePenaltyDetail()
        super.onDateClick()
    }

    override fun onClickFilterApplied(penaltyFilterUiModelList: List<PenaltyFilterUiModel>) {
        penaltyListListener?.closePenaltyDetail()
        super.onClickFilterApplied(penaltyFilterUiModelList)
    }

    override fun onChildSortFilterItemClick(sortFilterItem: SortFilterItem) {
        penaltyListListener?.closePenaltyDetail()
        super.onChildSortFilterItemClick(sortFilterItem)
    }

    override fun onSuccessGetPenaltyListData(data: List<ItemPenaltyUiModel>, hasNext: Boolean) {
        super.onSuccessGetPenaltyListData(data, hasNext)
    }

    fun setPenaltyListListener(listener: PenaltyListListener) {
        this.penaltyListListener = listener
    }

    interface PenaltyListListener {
        fun onItemPenaltyClicked(penaltyFilterUiModel: ItemPenaltyUiModel)
        fun closePenaltyDetail()
    }
}