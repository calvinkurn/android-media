package com.tokopedia.search.utils

import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import com.tokopedia.sortfilter.SortFilter

private const val SCROLL_THRESHOLD_TO_ANIMATE_TAB = 50

internal class HideTabOnScrollListener(
        private val context: Context?,
        private val searchNavigationListener: SearchNavigationListener?,
        private val sortFilter: SortFilter?
): RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (searchNavigationListener == null) return

        if (shouldConfigureTabLayout(dy, recyclerView)) {
            val isVisible = dy <= 0
            searchNavigationListener.configureTabLayout(isVisible)
        }

        if (recyclerView.computeVerticalScrollOffset() > 0) applyQuickFilterLayoutOnTop() else applyQuickFilterLayoutOnScroll()
    }

    private fun shouldConfigureTabLayout(dy: Int, recyclerView: RecyclerView) =
            dy > SCROLL_THRESHOLD_TO_ANIMATE_TAB
                    || dy < -SCROLL_THRESHOLD_TO_ANIMATE_TAB
                    || recyclerView.computeVerticalScrollOffset() == 0

    private fun applyQuickFilterLayoutOnTop() {
        if (context == null) return

        applyQuickFilterElevation(context)

        val paddingTop = 8.dpToPx(context.resources.displayMetrics)
        animateQuickFilterPaddingTopChanges(paddingTop)
    }

    private fun applyQuickFilterElevation(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && sortFilter != null) {
            if (sortFilter.elevation == 0f) {
                val elevation = 5.dpToPx(context.resources.displayMetrics)
                sortFilter.elevation = elevation.toFloat()
            }
        }
    }

    private fun animateQuickFilterPaddingTopChanges(paddingTop: Int) {
        if (sortFilter == null) return

        val anim = ValueAnimator.ofInt(sortFilter.paddingTop, paddingTop)
        anim.addUpdateListener { animator -> setQuickFilterPaddingTop(animator.animatedValue as Int) }
        anim.duration = 200
        anim.start()
    }

    private fun setQuickFilterPaddingTop(paddingTop: Int) {
        if (sortFilter == null || sortFilter.paddingTop == paddingTop) return

        sortFilter.setPadding(sortFilter.paddingLeft, paddingTop, sortFilter.paddingRight, sortFilter.paddingBottom)
    }

    private fun applyQuickFilterLayoutOnScroll() {
        if (context == null) return

        removeQuickFilterElevation()

        val paddingTop = 16.dpToPx(context.resources.displayMetrics)
        animateQuickFilterPaddingTopChanges(paddingTop)
    }

    private fun removeQuickFilterElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && sortFilter != null) {
            if (sortFilter.elevation > 0f) {
                sortFilter.elevation = 0f
            }
        }
    }
}