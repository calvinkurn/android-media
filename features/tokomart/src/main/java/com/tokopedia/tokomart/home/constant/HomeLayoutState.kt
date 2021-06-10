package com.tokopedia.tokomart.home.constant

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(
        HomeLayoutState.SHOW,
        HomeLayoutState.LOADING,
        HomeLayoutState.HIDE
)
annotation class HomeLayoutState {
    companion object {
        const val SHOW = "show"
        const val LOADING = "loading"
        const val HIDE = "hide"
    }
}