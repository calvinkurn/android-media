package com.tokopedia.shop.info.view.model

sealed interface ShopInfoUiEffect {
    data class RedirectToGmaps(val gmapsUrl: String) : ShopInfoUiEffect
    data class ShowShopLocationBottomSheet(val locations: List<String>) : ShopInfoUiEffect
    data class RedirectToShopReviewPage(val shopId: String) : ShopInfoUiEffect
    data class RedirectToReviewDetailPage(val reviewId: String) : ShopInfoUiEffect
    data class RedirectToShopNoteDetailPage(val shopId: String, val noteId: String) : ShopInfoUiEffect
}
