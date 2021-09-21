package com.tokopedia.tokopedianow.common.constant

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    TokoNowLayoutState.SHOW,
    TokoNowLayoutState.LOADING,
    TokoNowLayoutState.HIDE,
    TokoNowLayoutState.LOAD_MORE,
    TokoNowLayoutState.EMPTY,
    TokoNowLayoutState.UPDATE
)
annotation class TokoNowLayoutState {
    companion object {
        const val SHOW = 1
        const val LOADING = 2
        const val HIDE = 3
        const val LOAD_MORE = 4
        const val EMPTY = 6
        const val UPDATE = 7
    }
}