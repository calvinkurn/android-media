package com.tokopedia.carouselproductcard.helper

import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.carouselproductcard.R


/**
 * Created by Lukas on 15/10/19
 *
 * Example: https://mindorks.files.wordpress.com/2018/01/a92ca-1npi-wjo0ovasbkwv0gy5rw.gif
 *
 * [StartSnapHelper] is a helper class that helps in snapping any child view of the RecyclerView
 *
 * How to work?
 * snap the firstVisibleItem of the RecyclerView as you must have seen in the play store application
 * that the firstVisibleItem will be always completely visible when scrolling comes to the idle position
 *
 * Android already provides a helper class to do the same. The class is LinearSnapHelper which will only enable the center snapping.
 *
 * How to use?
 * val startHelper = StartSnapHelper()
 * startHelper.attachToRecyclerView(yourRecyclerView)
 */

class StartSnapHelper : LinearSnapHelper() {

    private lateinit var mVerticalHelper: OrientationHelper
    private lateinit var mHorizontalHelper: OrientationHelper

    @Throws(IllegalStateException::class)
    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
    }

    override fun calculateDistanceToFinalSnap(@NonNull layoutManager: RecyclerView.LayoutManager,
                                     @NonNull targetView: View): IntArray {
        val out = IntArray(2)

        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager))-
                    targetView.context.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16)
        } else {
            out[0] = 0
        }

        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager))
        } else {
            out[1] = 0
        }
        return out
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {

        return if (layoutManager is LinearLayoutManager) {

            if (layoutManager.canScrollHorizontally()) {
                getStartView(layoutManager, getHorizontalHelper(layoutManager))
            } else {
                getStartView(layoutManager, getVerticalHelper(layoutManager))
            }
        } else super.findSnapView(layoutManager)

    }

    private fun distanceToStart(targetView: View, helper: OrientationHelper): Int {
        return helper.getDecoratedStart(targetView) - helper.startAfterPadding
    }

    private fun getStartView(layoutManager: RecyclerView.LayoutManager,
                             helper: OrientationHelper): View? {

        if (layoutManager is LinearLayoutManager) {
            val firstChild = layoutManager.findFirstVisibleItemPosition()

            val isLastItem = layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1

            if (firstChild == RecyclerView.NO_POSITION || isLastItem) {
                return null
            }

            val child = layoutManager.findViewByPosition(firstChild)

            return if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2 && helper.getDecoratedEnd(child) > 0) {
                child
            } else {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                    null
                } else {
                    layoutManager.findViewByPosition(firstChild + 1)
                }
            }
        }

        return super.findSnapView(layoutManager)
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (!this::mVerticalHelper.isInitialized) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return mVerticalHelper
    }

    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (!this::mHorizontalHelper.isInitialized) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return mHorizontalHelper
    }
}
