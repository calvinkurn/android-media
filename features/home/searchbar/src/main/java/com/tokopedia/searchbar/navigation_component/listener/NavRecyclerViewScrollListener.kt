package com.tokopedia.searchbar.navigation_component.listener

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.Theme.TOOLBAR_DARK_TYPE
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE

class NavRecyclerViewScrollListener(
        val navToolbar: NavToolbar,
        var startTransitionPixel: Int = 0,
        var toolbarTransitionRangePixel: Int = 0,
        val navScrollCallback: NavScrollCallback? = null,
        val switchThemeOnScroll: Boolean = true,
        val fixedIconColor: Int? = null
): RecyclerView.OnScrollListener() {
    private val statusBarUtil = navToolbar.statusBarUtil

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (startTransitionPixel == 0) startTransitionPixel = navToolbar.resources.getDimensionPixelSize(R.dimen.default_nav_toolbar_start_transition)
        if (toolbarTransitionRangePixel == 0) toolbarTransitionRangePixel = navToolbar.resources.getDimensionPixelSize(R.dimen.default_nav_toolbar_transition_range)
        calculateNavToolbarTransparency(recyclerView.computeVerticalScrollOffset())
        val offset = recyclerView.computeVerticalScrollOffset();
        navScrollCallback?.onYposChanged(offset)

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
        if (fixedIconColor != null) {
            when (fixedIconColor) {
                TOOLBAR_DARK_TYPE -> navToolbar.switchToDarkToolbar()
                TOOLBAR_LIGHT_TYPE -> navToolbar.switchToLightToolbar()
            }
            darkModeCondition(
                    lightCondition = { statusBarUtil?.requestStatusBarLight() },
                    nightCondition = { statusBarUtil?.requestStatusBarDark() }
            )
            if (offsetAlpha >= 150) {
                navScrollCallback?.onSwitchToLightToolbar()
            } else {
                navScrollCallback?.onSwitchToDarkToolbar()
            }
        } else {
            if (offsetAlpha >= 150) {
                if (switchThemeOnScroll) {
                    navToolbar.switchToLightToolbar()
                    darkModeCondition(
                            lightCondition = { statusBarUtil?.requestStatusBarLight() },
                            nightCondition = { statusBarUtil?.requestStatusBarDark() }
                    )
                }
                navScrollCallback?.onSwitchToLightToolbar()
            } else {
                if (switchThemeOnScroll) {
                    navToolbar.switchToDarkToolbar()
                    darkModeCondition(
                            lightCondition = { statusBarUtil?.requestStatusBarDark() },
                            nightCondition = { statusBarUtil?.requestStatusBarLight() }
                    )
                }
                navScrollCallback?.onSwitchToDarkToolbar()
            }
        }
        if (offsetAlpha >= 255) {
            offsetAlpha = 255f
        }
        if (offsetAlpha >= 0 && offsetAlpha <= 255) {
            navToolbar.setBackgroundAlpha(offsetAlpha)
            navScrollCallback?.onAlphaChanged(offsetAlpha)
        }
    }

    private fun darkModeCondition(lightCondition: () -> Unit = {}, nightCondition:() -> Unit = {}) {
        val isNightMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        if (!isNightMode) lightCondition.invoke()
        if (isNightMode) nightCondition.invoke()
    }

    interface NavScrollCallback {
        fun onAlphaChanged(offsetAlpha: Float)
        fun onSwitchToDarkToolbar()
        fun onSwitchToLightToolbar()
        fun onYposChanged(yOffset: Int)
    }
}