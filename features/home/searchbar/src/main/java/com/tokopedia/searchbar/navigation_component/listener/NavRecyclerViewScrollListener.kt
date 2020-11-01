package com.tokopedia.searchbar.navigation_component.listener

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.navigation_component.NavToolbar

class NavRecyclerViewScrollListener(
        val navToolbar: NavToolbar,
        var startTransitionPixel: Int = 0,
        var toolbarTransitionRangePixel: Int = 0,
        val navScrollCallback: NavScrollCallback? = null
): RecyclerView.OnScrollListener() {
    private val statusBarUtil = navToolbar.statusBarUtil
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (startTransitionPixel == 0) startTransitionPixel = navToolbar.resources.getDimensionPixelSize(R.dimen.default_nav_toolbar_start_transition)
        if (toolbarTransitionRangePixel == 0) toolbarTransitionRangePixel = navToolbar.resources.getDimensionPixelSize(R.dimen.default_nav_toolbar_transition_range)

        calculateNavToolbarTransparency(recyclerView.computeVerticalScrollOffset())
    }

    private fun calculateNavToolbarTransparency(offset: Int) {
        val endTransitionOffset = startTransitionPixel + toolbarTransitionRangePixel
        val maxTransitionOffset = endTransitionOffset - startTransitionPixel
        //mapping alpha to be rendered per pixel for x height
        var offsetAlpha = 255f / maxTransitionOffset * (offset - startTransitionPixel)
        //2.5 is maximum
        if (offsetAlpha < 0) {
            offsetAlpha = 0f
        }
        if (offsetAlpha >= 150) {
            navToolbar.switchToLightToolbar()
            statusBarUtil?.requestStatusBarLight()
            navScrollCallback?.onSwitchToLightToolbar()
        } else {
            navToolbar.switchToDarkToolbar()
            statusBarUtil?.requestStatusBarDark()
            navScrollCallback?.onSwitchToDarkToolbar()
        }
        if (offsetAlpha >= 255) {
            offsetAlpha = 255f
        }
        if (offsetAlpha >= 0 && offsetAlpha <= 255) {
            navToolbar.setBackgroundAlpha(offsetAlpha)
            navScrollCallback?.onAlphaChanged(offsetAlpha)
        }
    }

    interface NavScrollCallback {
        fun onAlphaChanged(offsetAlpha: Float)
        fun onSwitchToDarkToolbar()
        fun onSwitchToLightToolbar()
    }
}