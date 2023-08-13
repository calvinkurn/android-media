package com.tokopedia.promousage.view.viewmodel

import com.tokopedia.promousage.domain.entity.PromoCta
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem

data class LoadingUiAction(
    val isLoading: Boolean = false
)

data class PromoRecommendationUiAction(
    val recommendationItem: PromoRecommendationItem? = null
)

data class PromoRecommendationPerformClickAction(
    val itemIndex: List<Int> = emptyList()
)

data class PromoListUiAction(
    val action: Int = 0,
    val exception: Throwable? = null
) {
    companion object {
        const val ACTION_SHOW_ERROR_TOAST = 1
    }
}

data class PromoAttemptUiAction(
    val state: State = State.NONE,
    val errorMessage: String = ""
) {
    enum class State {
        NONE, SHOW_ERROR_TOAST
    }
}

data class ApplyPromoUiAction(
    val state: State = State.NONE,
    val throwable: Throwable? = null
) {
    enum class State {
        NONE, SHOW_ERROR_TOAST, SHOW_ERROR_TOAST_AND_RELOAD
    }
}

data class PromoCtaUiAction(
    val promoCta: PromoCta? = null
)

