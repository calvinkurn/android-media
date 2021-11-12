package com.tokopedia.shop.score.penalty.presentation.fragment.tablet

import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageFragment
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.sortfilter.SortFilterItem

class ShopPenaltyListTabletFragment : ShopPenaltyPageFragment() {

    private var penaltyListListener: PenaltyListListener? = null

    override fun onItemPenaltyClick(itemPenaltyUiModel: ItemPenaltyUiModel, position: Int) {
        context?.let {
            penaltyPageAdapter.updateSelectedBackground(position)
            penaltyListListener?.onItemPenaltyClicked(itemPenaltyUiModel)
        } ?: super.onItemPenaltyClick(itemPenaltyUiModel, position)
    }

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        penaltyListListener?.closePenaltyDetail()
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

    fun setPenaltyListListener(listener: PenaltyListListener) {
        this.penaltyListListener = listener
    }

    interface PenaltyListListener {
        fun onItemPenaltyClicked(penaltyFilterUiModel: ItemPenaltyUiModel)
        fun closePenaltyDetail()
    }
}