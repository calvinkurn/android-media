package com.tokopedia.shop.info.view.model

import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopReview

sealed interface ShopInfoUiEvent {
    data class Setup(val shopId: String, val districtId: String, val cityId: String) :
        ShopInfoUiEvent

    object GetShopInfo : ShopInfoUiEvent
    object TapShopRating : ShopInfoUiEvent
    data class TapReviewImage(
        val review: ShopReview.Review,
        val reviewImageIndex: Int
    ) : ShopInfoUiEvent

    data class TapReviewImageViewAll(val productId: String) : ShopInfoUiEvent
    object TapCtaExpandShopPharmacyInfo : ShopInfoUiEvent

    data class SwipeReview(val reviewIndex: Int) : ShopInfoUiEvent

    object TapCtaViewPharmacyLocation : ShopInfoUiEvent
    data class TapShopNote(val shopNote: ShopNote) : ShopInfoUiEvent
    object RetryGetShopInfo : ShopInfoUiEvent
    object ReportShop : ShopInfoUiEvent
}
