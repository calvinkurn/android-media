package com.tokopedia.tokofood.home.domain.constanta

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    TokoFoodLayoutState.SHOW,
    TokoFoodLayoutState.LOADING,
    TokoFoodLayoutState.HIDE,
    TokoFoodLayoutState.LOAD_MORE,
    TokoFoodLayoutState.LOADED,
    TokoFoodLayoutState.EMPTY,
    TokoFoodLayoutState.UPDATE
)
annotation class TokoFoodLayoutState {
    companion object {
        const val SHOW = 1
        const val LOADING = 2
        const val HIDE = 3
        const val LOAD_MORE = 4
        const val LOADED = 5
        const val EMPTY = 6
        const val UPDATE = 7
    }
}