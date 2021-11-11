package com.tokopedia.shop.score.penalty.presentation.fragment.tablet

import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageFragment
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel

class ShopPenaltyListTabletFragment: ShopPenaltyPageFragment() {

    override fun onItemPenaltyClick(itemPenaltyUiModel: ItemPenaltyUiModel) {
        context?.let {
            if (DeviceScreenInfo.isTablet(it)) {

            } else {
                super.onItemPenaltyClick(itemPenaltyUiModel)
            }
        } ?: super.onItemPenaltyClick(itemPenaltyUiModel)
    }

    interface SomListClickListener {
        fun closeOrderDetail()
    }
}