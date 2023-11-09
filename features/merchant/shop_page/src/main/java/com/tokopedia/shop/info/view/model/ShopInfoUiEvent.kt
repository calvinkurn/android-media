package com.tokopedia.shop.info.view.model

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

sealed interface ShopInfoUiEvent {
    data class GetShopInfo(val shopId: String, val localCacheModel: LocalCacheModel) : ShopInfoUiEvent
    object TapIconViewAllShopReview : ShopInfoUiEvent
    object TapCtaExpandShopPharmacyInfo : ShopInfoUiEvent
    object TapCtaViewPharmacyLocation : ShopInfoUiEvent
    data class TapShopNote(val noteId: String) : ShopInfoUiEvent
    data class RetryGetShopInfo(val localCacheModel: LocalCacheModel) : ShopInfoUiEvent
    object ReportShop : ShopInfoUiEvent
}
