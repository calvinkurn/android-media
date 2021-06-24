package com.tokopedia.tokopedianow.home.constant

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(
        HomeLayoutState.SHOW,
        HomeLayoutState.LOADING,
        HomeLayoutState.HIDE
)
annotation class HomeLayoutState {
    companion object {
        const val SHOW = 1
        const val LOADING = 2
        const val HIDE = 3
    }
}