/*
 * Copyright 2018 The Android Open Source Project
 * Copyright 2019 Rúben Sousa
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tokopedia.play_common.widget.playBannerCarousel.helper

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.annotation.Px
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.*
import java.util.*
import kotlin.math.abs
import kotlin.math.max

/**
 * A [LinearSnapHelper] that allows snapping to an edge or to the center.
 *
 *
 * Possible snap positions:
 * [Gravity.START], [Gravity.TOP], [Gravity.END], [Gravity.BOTTOM],
 * [Gravity.CENTER].
 *
 *
 *
 * To customize the maximum scroll distance during flings,
 * use [GravitySnapHelper.setMaxFlingSizeFraction]
 * or [GravitySnapHelper.setMaxFlingDistance]
 */

@SuppressLint("SyntheticAccessor")
class GravitySnapHelper @JvmOverloads constructor(gravity: Int, enableSnapLastItem: Boolean = false,
                                                  snapListener: SnapListener? = null) : LinearSnapHelper() {
    private var gravity: Int
    private var isRtl = false
    private var snapLastItem: Boolean
    private var nextSnapPosition = 0
    private var isScrolling = false
    private var snapToPadding = false
    private var scrollMsPerInch = 80f
    private var maxFlingDistance = FLING_DISTANCE_DISABLE
    private var maxFlingSizeFraction = FLING_SIZE_FRACTION_DISABLE
    private var verticalHelper: OrientationHelper? = null
    private var horizontalHelper: OrientationHelper? = null
    private var listener: SnapListener?
    private var recyclerView: RecyclerView? = null
    private val scrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            this@GravitySnapHelper.onScrollStateChanged(newState)
        }
    }

    constructor(gravity: Int, snapListener: SnapListener) : this(gravity, false, snapListener)

    @Throws(IllegalStateException::class)
    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        if (this.recyclerView != null) {
            this.recyclerView!!.removeOnScrollListener(scrollListener)
        }
        if (recyclerView != null) {
            recyclerView.onFlingListener = null
            if (gravity == Gravity.START || gravity == Gravity.END) {
                isRtl = (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault())
                        == ViewCompat.LAYOUT_DIRECTION_RTL)
            }
            recyclerView.addOnScrollListener(scrollListener)
            this.recyclerView = recyclerView
        } else {
            this.recyclerView = null
        }
        super.attachToRecyclerView(recyclerView)
    }

    override fun findSnapView(lm: RecyclerView.LayoutManager): View? {
        return findSnapView(lm, true)
    }

    private fun findSnapView(lm: RecyclerView.LayoutManager, checkEdgeOfList: Boolean): View? {
        var snapView: View? = null
        when (gravity) {
            Gravity.START -> snapView = findView(lm, getHorizontalHelper(lm)!!, Gravity.START, checkEdgeOfList)
            Gravity.END -> snapView = findView(lm, getHorizontalHelper(lm)!!, Gravity.END, checkEdgeOfList)
            Gravity.TOP -> snapView = findView(lm, getVerticalHelper(lm)!!, Gravity.START, checkEdgeOfList)
            Gravity.BOTTOM -> snapView = findView(lm, getVerticalHelper(lm)!!, Gravity.END, checkEdgeOfList)
            Gravity.CENTER -> snapView = if (lm.canScrollHorizontally()) {
                findView(lm, getHorizontalHelper(lm)!!, Gravity.CENTER,
                        checkEdgeOfList)
            } else {
                findView(lm, getVerticalHelper(lm)!!, Gravity.CENTER,
                        checkEdgeOfList)
            }
        }
        nextSnapPosition = if (snapView != null) {
            recyclerView!!.getChildAdapterPosition(snapView)
        } else {
            RecyclerView.NO_POSITION
        }
        return snapView
    }

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager,
                                              targetView: View): IntArray {
        if (gravity == Gravity.CENTER) {
            return super.calculateDistanceToFinalSnap(layoutManager, targetView)!!
        }
        val out = IntArray(2)
        if (layoutManager !is LinearLayoutManager) {
            return out
        }
        if (layoutManager.canScrollHorizontally()) {
            if (isRtl && gravity == Gravity.END || !isRtl && gravity == Gravity.START) {
                out[0] = getDistanceToStart(targetView, getHorizontalHelper(layoutManager)!!)
            } else {
                out[0] = getDistanceToEnd(targetView, getHorizontalHelper(layoutManager)!!)
            }
        } else if (layoutManager.canScrollVertically()) {
            if (gravity == Gravity.TOP) {
                out[1] = getDistanceToStart(targetView, getVerticalHelper(layoutManager)!!)
            } else {
                out[1] = getDistanceToEnd(targetView, getVerticalHelper(layoutManager)!!)
            }
        }
        return out
    }

    override fun calculateScrollDistance(velocityX: Int, velocityY: Int): IntArray {
        if (recyclerView == null || verticalHelper == null && horizontalHelper == null
                || (maxFlingDistance == FLING_DISTANCE_DISABLE
                        && maxFlingSizeFraction == FLING_SIZE_FRACTION_DISABLE)) {
            return super.calculateScrollDistance(velocityX, velocityY)
        }
        val out = IntArray(2)
        val scroller = Scroller(recyclerView!!.context,
                DecelerateInterpolator())
        val maxDistance = getFlingDistance()
        scroller.fling(0, 0, velocityX, velocityY,
                -maxDistance, maxDistance,
                -maxDistance, maxDistance)
        out[0] = scroller.finalX
        out[1] = scroller.finalY
        return out
    }

    override fun createScroller(layoutManager: RecyclerView.LayoutManager): RecyclerView.SmoothScroller? {
        return if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider
                || recyclerView == null) {
            null
        } else object : LinearSmoothScroller(recyclerView!!.context) {
            override fun onTargetFound(targetView: View,
                                       state: RecyclerView.State,
                                       action: Action) {
                if (recyclerView == null || recyclerView!!.layoutManager == null) {
                    // The associated RecyclerView has been removed so there is no action to take.
                    return
                }
                val snapDistances = calculateDistanceToFinalSnap(recyclerView!!.layoutManager!!,
                        targetView)
                val dx = snapDistances[0]
                val dy = snapDistances[1]
                val time = calculateTimeForDeceleration(max(abs(dx), abs(dy)))
                if (time > 0) {
                    action.update(dx, dy, time, mDecelerateInterpolator)
                }
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return scrollMsPerInch / displayMetrics.densityDpi
            }

        }
    }

    /**
     * Changes the max fling distance in absolute values.
     *
     * @param distance max fling distance in pixels
     * or [GravitySnapHelper.FLING_DISTANCE_DISABLE]
     * to disable fling limits
     */
    private fun setMaxFlingDistance(@Px distance: Int) {
        maxFlingDistance = distance
        maxFlingSizeFraction = FLING_SIZE_FRACTION_DISABLE
    }

    /**
     * Changes the max fling distance depending on the available size of the RecyclerView.
     *
     *
     * Example: if you pass 0.5f and the RecyclerView measures 600dp,
     * the max fling distance will be 300dp.
     *
     * @param fraction size fraction to be used for the max fling distance
     * or [GravitySnapHelper.FLING_SIZE_FRACTION_DISABLE]
     * to disable fling limits
     */
    private fun setMaxFlingSizeFraction(fraction: Float) {
        maxFlingDistance = FLING_DISTANCE_DISABLE
        maxFlingSizeFraction = fraction
    }

    private fun getFlingDistance(): Int {
        return when {
            maxFlingSizeFraction != FLING_SIZE_FRACTION_DISABLE -> {
                when {
                    verticalHelper != null -> {
                        (recyclerView!!.height * maxFlingSizeFraction).toInt()
                    }
                    horizontalHelper != null -> {
                        (recyclerView!!.width * maxFlingSizeFraction).toInt()
                    }
                    else -> {
                        Int.MAX_VALUE
                    }
                }
            }
            maxFlingDistance != FLING_DISTANCE_DISABLE -> {
                maxFlingDistance
            }
            else -> {
                Int.MAX_VALUE
            }
        }
    }

    private fun getDistanceToStart(targetView: View, helper: OrientationHelper): Int {
        val distance: Int
        // If we don't care about padding, just snap to the start of the view
        distance = if (!snapToPadding) {
            val childStart = helper.getDecoratedStart(targetView)
            if (childStart >= helper.startAfterPadding / 2) {
                childStart - helper.startAfterPadding
            } else {
                childStart
            }
        } else {
            helper.getDecoratedStart(targetView) - helper.startAfterPadding
        }
        return distance
    }

    private fun getDistanceToEnd(targetView: View, helper: OrientationHelper): Int {
        val distance: Int
        distance = if (!snapToPadding) {
            val childEnd = helper.getDecoratedEnd(targetView)
            if (childEnd >= helper.end - (helper.end - helper.endAfterPadding) / 2) {
                helper.getDecoratedEnd(targetView) - helper.end
            } else {
                childEnd - helper.endAfterPadding
            }
        } else {
            helper.getDecoratedEnd(targetView) - helper.endAfterPadding
        }
        return distance
    }

    /**
     * Returns the first view that we should snap to.
     *
     * @param layoutManager the RecyclerView's LayoutManager
     * @param helper        orientation helper to calculate view sizes
     * @param gravity       gravity to find the closest view
     * @return the first view in the LayoutManager to snap to, or null if we shouldn't snap to any
     */
    private fun findView(layoutManager: RecyclerView.LayoutManager,
                         helper: OrientationHelper,
                         gravity: Int,
                         checkEdgeOfList: Boolean): View? {
        if (layoutManager.childCount == 0 || layoutManager !is LinearLayoutManager) {
            return null
        }

        // If we're at an edge of the list, we shouldn't snap
        // to avoid having the last item not completely visible.
        if (checkEdgeOfList && isAtEdgeOfList(layoutManager) && !snapLastItem) {
            return null
        }
        var edgeView: View? = null
        var distanceToTarget = Int.MAX_VALUE
        val center: Int = if (layoutManager.getClipToPadding()) {
            helper.startAfterPadding + helper.totalSpace / 2
        } else {
            helper.end / 2
        }
        val snapToStart = (gravity == Gravity.START && !isRtl
                || gravity == Gravity.END && isRtl)
        val snapToEnd = (gravity == Gravity.START && isRtl
                || gravity == Gravity.END && !isRtl)
        for (i in 0 until layoutManager.childCount) {
            val currentView = layoutManager.getChildAt(i)
            var currentViewDistance: Int
            currentViewDistance = if (snapToStart) {
                if (!snapToPadding) {
                    abs(helper.getDecoratedStart(currentView))
                } else {
                    abs(helper.startAfterPadding
                            - helper.getDecoratedStart(currentView))
                }
            } else if (snapToEnd) {
                if (!snapToPadding) {
                    abs(helper.getDecoratedEnd(currentView)
                            - helper.end)
                } else {
                    abs(helper.endAfterPadding
                            - helper.getDecoratedEnd(currentView))
                }
            } else {
                abs(helper.getDecoratedStart(currentView)
                        + helper.getDecoratedMeasurement(currentView) / 2 - center)
            }
            if (currentViewDistance < distanceToTarget) {
                distanceToTarget = currentViewDistance
                edgeView = currentView
            }
        }
        return edgeView
    }

    private fun isAtEdgeOfList(lm: LinearLayoutManager): Boolean {
        return if (!lm.reverseLayout && gravity == Gravity.START
                || lm.reverseLayout && gravity == Gravity.END
                || !lm.reverseLayout && gravity == Gravity.TOP
                || lm.reverseLayout && gravity == Gravity.BOTTOM) {
            lm.findLastCompletelyVisibleItemPosition() == lm.itemCount - 1
        } else if (gravity == Gravity.CENTER) {
            (lm.findFirstCompletelyVisibleItemPosition() == 0
                    || lm.findLastCompletelyVisibleItemPosition() == lm.itemCount - 1)
        } else {
            lm.findFirstCompletelyVisibleItemPosition() == 0
        }
    }

    /**
     * Dispatches a [SnapListener.onSnap] event if the snapped position
     * is different than [RecyclerView.NO_POSITION].
     *
     *
     * When [GravitySnapHelper.findSnapView] returns null,
     * [GravitySnapHelper.dispatchSnapChangeWhenPositionIsUnknown] is called
     *
     * @param newState the new RecyclerView scroll state
     */
    internal fun onScrollStateChanged(newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && listener != null) {
            if (isScrolling) {
                if (nextSnapPosition != RecyclerView.NO_POSITION) {
                    listener?.onSnap(recyclerView, nextSnapPosition)
                } else {
                    dispatchSnapChangeWhenPositionIsUnknown()
                }
            }
        }
        isScrolling = newState != RecyclerView.SCROLL_STATE_IDLE
    }

    /**
     * Calls [GravitySnapHelper.findSnapView]
     * without the check for the edge of the list.
     *
     *
     * This makes sure that a position is reported in [SnapListener.onSnap]
     */
    private fun dispatchSnapChangeWhenPositionIsUnknown() {
        val layoutManager = recyclerView!!.layoutManager ?: return
        val snapView = findSnapView(layoutManager, false) ?: return
        val snapPosition = recyclerView!!.getChildAdapterPosition(snapView)
        if (snapPosition != RecyclerView.NO_POSITION) {
            listener?.onSnap(recyclerView, snapPosition)
        }
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper? {
        if (verticalHelper == null || verticalHelper!!.layoutManager !== layoutManager) {
            verticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return verticalHelper
    }

    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper? {
        if (horizontalHelper == null || horizontalHelper!!.layoutManager !== layoutManager) {
            horizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return horizontalHelper
    }

    /**
     * A listener that's called when the [RecyclerView] used by [GravitySnapHelper]
     * changes its scroll state to [RecyclerView.SCROLL_STATE_IDLE]
     * and there's a valid snap position.
     */
    interface SnapListener {
        /**
         * @param position last position snapped to
         */
        fun onSnap(recyclerView: RecyclerView?, position: Int)
    }

    companion object {
        const val FLING_DISTANCE_DISABLE = -1
        const val FLING_SIZE_FRACTION_DISABLE = -1f
    }

    init {
        require(!(gravity != Gravity.START && gravity != Gravity.END && gravity != Gravity.BOTTOM && gravity != Gravity.TOP && gravity != Gravity.CENTER)) {
            "Invalid gravity value. Use START " +
                    "| END | BOTTOM | TOP | CENTER constants"
        }
        snapLastItem = enableSnapLastItem
        this.gravity = gravity
        listener = snapListener
    }
}