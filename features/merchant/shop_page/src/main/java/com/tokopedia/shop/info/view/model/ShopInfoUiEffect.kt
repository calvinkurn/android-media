package com.tokopedia.shop.info.view.model

import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopReview

sealed interface ShopInfoUiEffect {
    data class RedirectToGmaps(val gmapsUrl: String) : ShopInfoUiEffect
    data class RedirectToShopReviewPage(val shopId: String) : ShopInfoUiEffect
    data class RedirectToProductReviewPage(
        val review: ShopReview.Review,
        val shopId: String,
        val reviewImageIndex: Int
    ) : ShopInfoUiEffect

    data class RedirectToProductReviewGallery(val productId: String) : ShopInfoUiEffect
    data class ShowShopNoteDetailBottomSheet(val shopNote: ShopNote) : ShopInfoUiEffect
    object RedirectToLoginPage : ShopInfoUiEffect
    data class RedirectToChatWebView(val messageId: String) : ShopInfoUiEffect
}
