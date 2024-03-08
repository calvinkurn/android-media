package com.tokopedia.bmsm_widget.presentation.bottomsheet.uimodel

import com.tokopedia.bmsm_widget.domain.entity.PageSource
import com.tokopedia.bmsm_widget.domain.entity.TierGift
import com.tokopedia.bmsm_widget.domain.entity.TierGifts
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

sealed interface GiftListEvent {
    data class OpenScreen(
        val offerId: Long,
        val warehouseId: Long,
        val giftProducts: List<TierGifts>,
        val source: PageSource,
        val selectedTierId: Long,
        val userCache: LocalCacheModel,
        val shopId: String
    ) : GiftListEvent

    object GetGiftList : GiftListEvent
    data class ChangeGiftTier(val selectedTier: TierGift) : GiftListEvent

    object TapIconCloseBottomSheet : GiftListEvent
}
