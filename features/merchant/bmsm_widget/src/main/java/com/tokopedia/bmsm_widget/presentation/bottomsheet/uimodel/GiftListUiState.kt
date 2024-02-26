package com.tokopedia.bmsm_widget.presentation.bottomsheet.uimodel

import com.tokopedia.bmsm_widget.domain.entity.MainProduct
import com.tokopedia.bmsm_widget.domain.entity.PageSource
import com.tokopedia.bmsm_widget.domain.entity.TierGift
import com.tokopedia.bmsm_widget.domain.entity.TierGifts
import com.tokopedia.bmsm_widget.util.constant.BundleConstant
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

data class GiftListUiState(
    val isLoading: Boolean = false,
    val offerId: Long = 0,
    val warehouseId: Long = 0,
    val source: PageSource = PageSource.OFFER_LANDING_PAGE,
    val selectedTierId: Long = BundleConstant.ID_NO_SELECTED_TIER,
    val tierGift: List<TierGift> = emptyList(),
    val tierProducts: List<TierGifts> = emptyList(),
    val mainProducts: List<MainProduct> = emptyList(),
    val selectedTier: TierGift? = null,
    val error: Throwable? = null,
    val userCache: LocalCacheModel = LocalCacheModel(),
    val shopId: String = ""
)
