package com.tokopedia.product.detail.view.util

import android.support.design.widget.AppBarLayout

abstract class AppBarStateChangeListener: AppBarLayout.OnOffsetChangedListener {

    @AppBarState
    private var state = AppBarState.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (appBarLayout == null)
            return
        state = when {
            verticalOffset == 0 -> {
                if (state != AppBarState.EXPANDED) onStateChanged(appBarLayout, AppBarState.EXPANDED)
                AppBarState.EXPANDED
            }
            Math.abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                if (state != AppBarState.COLLAPSED) onStateChanged(appBarLayout, AppBarState.COLLAPSED)
                AppBarState.COLLAPSED
            }
            else -> {
                if (state != AppBarState.IDLE) onStateChanged(appBarLayout, AppBarState.IDLE)
                AppBarState.IDLE
            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout?, @AppBarState state: Int)
}