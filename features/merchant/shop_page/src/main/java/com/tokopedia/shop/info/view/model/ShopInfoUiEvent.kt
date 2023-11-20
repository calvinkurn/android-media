package com.tokopedia.shop.info.view.model

import com.tokopedia.shop.info.domain.entity.ShopNote

sealed interface ShopInfoUiEvent {
    data class Setup(val shopId: String, val districtId: String, val cityId: String) :
        ShopInfoUiEvent

    object GetShopInfo : ShopInfoUiEvent
    object TapIconViewAllShopReview : ShopInfoUiEvent
    data class TapReviewImage(val productId: String, val reviewImageIndex: Int) : ShopInfoUiEvent
    data class TapReviewImageViewAll(val productId: String) : ShopInfoUiEvent
    object TapCtaExpandShopPharmacyInfo : ShopInfoUiEvent

    object TapCtaViewPharmacyLocation : ShopInfoUiEvent
    data class TapShopNote(val shopNote: ShopNote) : ShopInfoUiEvent
    object RetryGetShopInfo : ShopInfoUiEvent
    object ReportShop : ShopInfoUiEvent
}
