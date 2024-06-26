package com.tokopedia.promousage.util.composite

sealed class DelegatePayload {

    data class UpdatePromoRecommendation(
        val isReload: Boolean = true,
        val isPromoStateUpdated: Boolean = true,
        val isPromoStartAnimating: Boolean = false
    ) : DelegatePayload()

    data class UpdatePromo(
        val isReload: Boolean = true,
        val isPromoStateUpdated: Boolean = true
    ) : DelegatePayload()
}
