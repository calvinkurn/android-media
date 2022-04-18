package com.tokopedia.tokofood.home.domain.constanta

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    TokoFoodHomeLayoutState.SHOW,
    TokoFoodHomeLayoutState.LOADING,
    TokoFoodHomeLayoutState.HIDE,
    TokoFoodHomeLayoutState.LOAD_MORE,
    TokoFoodHomeLayoutState.LOADED,
    TokoFoodHomeLayoutState.EMPTY,
    TokoFoodHomeLayoutState.UPDATE
)
annotation class TokoFoodHomeLayoutState {
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