package com.tokopedia.tokopedianow.common.view

import android.app.Activity
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipehome.presentation.fragment.TokoNowRecipeHomeFragment
import com.tokopedia.unifycomponents.ImageUnify

class TokoNowNavToolbar(
    private val activity: Activity?,
    private val navToolbar: NavToolbar?,
    private val pageName: String,
    private val hintData: List<HintData> = emptyList(),
    private val onClickSearchBar: (hint: String) -> Unit
) {

    companion object {
        const val DEFAULT_POSITION = 0
    }

    var headerBackground: ImageUnify? = null
    var scrollListener: RecyclerView.OnScrollListener? = null
    var navShadowScrollListener: RecyclerView.OnScrollListener? = null

    private val toolbarHeight: Int
        get() {
            val defaultHeight =
                getDimensionPixelSize(R.dimen.tokopedianow_default_toolbar_status_height)

            val height = (navToolbar?.height ?: defaultHeight)
            val padding = getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

            return height + padding
        }

    private var movingPosition = 0

    fun init(fragment: Fragment) {
        val navToolbar = navToolbar ?: return

        navToolbar.apply {
            bringToFront()
            setToolbarPageName(pageName)
            setupSearchbar(
                hints = hintData,
                searchbarClickCallback = onClickSearchBar,
                disableDefaultGtmTracker = true,
            )

        }

        fragment.activity?.let {
            navToolbar.setupToolbarWithStatusBar(activity = it)
        }

        fragment.lifecycle.addObserver(navToolbar)

        setupNavShadowScrollListener()
        setupScrollListener()
    }

    fun setIcon(iconBuilder: IconBuilder) {
        navToolbar?.setIcon(iconBuilder)
    }

    fun setSearchbarText(text: String) {
        navToolbar?.setSearchbarText(text)
    }

    fun setBackButtonOnClickListener(onClickListener: () -> Unit) {
        navToolbar?.setOnBackButtonClickListener(true) {
            onClickListener.invoke()
        }
    }

    private fun setupScrollListener() {
        navToolbar?.let {
            scrollListener = NavRecyclerViewScrollListener(
                navToolbar = it,
                startTransitionPixel = toolbarHeight,
                toolbarTransitionRangePixel = getDimensionPixelSize(R.dimen.tokopedianow_searchbar_transition_range),
                navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                    override fun onAlphaChanged(offsetAlpha: Float) {

                    }

                    override fun onSwitchToLightToolbar() {
                        if (pageName == TokoNowRecipeHomeFragment.PAGE_NAME) {
                            navToolbar.switchToNormalBackground(activity)
                        }
                    }

                    override fun onSwitchToDarkToolbar() {
                        if (pageName == TokoNowRecipeHomeFragment.PAGE_NAME) {
                            navToolbar.switchToStaticBackground(activity)
                        }
                    }

                    override fun onYposChanged(yOffset: Int) {

                    }
                },
                fixedIconColor = NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE
            )
        }
    }

    private fun setupNavShadowScrollListener() {
        navShadowScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                movingPosition += dy
                headerBackground?.y = if (movingPosition >= DEFAULT_POSITION) {
                    -(movingPosition.toFloat())
                } else {
                    resetMovingPosition()
                    movingPosition.toFloat()
                }
                val canScrollVertically = recyclerView.canScrollVertically(1)
                if (canScrollVertically || movingPosition != DEFAULT_POSITION) {
                    navToolbar?.showShadow(lineShadow = false)
                } else {
                    navToolbar?.hideShadow(lineShadow = false)
                }
            }
        }
    }

    private fun resetMovingPosition() {
        movingPosition = DEFAULT_POSITION
    }

    private fun getDimensionPixelSize(@DimenRes resId: Int): Int {
        return activity?.resources?.getDimensionPixelSize(resId).orZero()
    }
}
