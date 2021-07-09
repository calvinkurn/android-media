package com.tokopedia.topupbills.telco.prepaid.widget

import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.Px
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.*
import java.util.*
import kotlin.jvm.Throws
import kotlin.math.abs


/**
 * Created by Lukas on 15/10/19
 * A {@link LinearSnapHelper} that allows snapping to an edge or to the center.
 * <p>
 * Possible snap positions:
 * {@link Gravity#START}, {@link Gravity#TOP}, {@link Gravity#END}, {@link Gravity#BOTTOM},
 * {@link Gravity#CENTER}.
 * <p>
 * To customize the scroll duration, use {@link GravitySnapHelper#setScrollMsPerInch(float)}.
 * <p>
 * To customize the maximum scroll distance during flings,
 * use {@link GravitySnapHelper#setMaxFlingSizeFraction(float)}
 * or {@link GravitySnapHelper#setMaxFlingDistance(int)}
 */


open class GravitySnapHelper @JvmOverloads constructor(private var gravity: Int,
        /**
         * @return true if this SnapHelper should snap to the last item
         */
                                                  /**
                                                   * Enable snapping of the last item that's snappable.
                                                   * The default value is false, because you can't see the last item completely
                                                   * if this is enabled.
                                                   *
                                                   * @param snap true if you want to enable snapping of the last snappable item
                                                   */
                                                   private var snapLastItem: Boolean = false) : LinearSnapHelper() {
    private var isRtl: Boolean = false
    private var nextSnapPosition: Int = 0
    internal var isScrolling = false
    /**
     * @return true if this SnapHelper should snap to the padding. Defaults to false.
     */
    /**
     * If true, GravitySnapHelper will snap to the gravity edge
     * plus any amount of padding that was set in the RecyclerView.
     *
     *
     * The default value is false.
     *
     * @param snapToPadding true if you want to snap to the padding
     */
    var snapToPadding = false
    /**
     * @return last scroll speed set through [GravitySnapHelper.setScrollMsPerInch]
     * or 100f
     */
    /**
     * Sets the scroll duration in ms per inch.
     *
     *
     * Default value is 100.0f
     *
     *
     * This value will be used in
     * [GravitySnapHelper.createScroller]
     *
     * @param ms scroll duration in ms per inch
     */
    var scrollMsPerInch = 100f
    private var maxFlingDistance = FLING_DISTANCE_DISABLE
    private var maxFlingSizeFraction = FLING_SIZE_FRACTION_DISABLE
    private lateinit var verticalHelper: OrientationHelper
    private lateinit var horizontalHelper: OrientationHelper
    internal lateinit var listener: SnapListener
    internal lateinit var recyclerView: RecyclerView

    /**
     * @return the position of the current view that's snapped
     * or [RecyclerView.NO_POSITION] in case there's none.
     */
    val currentSnappedPosition: Int
        get() {
            if (this::recyclerView.isInitialized && recyclerView.layoutManager != null) {
                recyclerView.layoutManager?.let {
                    val snappedView = findSnapView(it)
                    if (snappedView != null) {
                        return recyclerView.getChildAdapterPosition(snappedView)
                    }
                }
            }
            return RecyclerView.NO_POSITION
        }

    private val flingDistance: Int
        get() = when {
            maxFlingSizeFraction != FLING_SIZE_FRACTION_DISABLE -> when {
                this::verticalHelper.isInitialized -> (recyclerView.height * maxFlingSizeFraction).toInt()
                this::horizontalHelper.isInitialized -> (recyclerView.width * maxFlingSizeFraction).toInt()
                else -> Integer.MAX_VALUE
            }
            maxFlingDistance != FLING_DISTANCE_DISABLE -> maxFlingDistance
            else -> Integer.MAX_VALUE
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
        fun onSnap(position: Int)
    }

    init {
        require(!(gravity != Gravity.START
                && gravity != Gravity.END
                && gravity != Gravity.BOTTOM
                && gravity != Gravity.TOP
                && gravity != Gravity.CENTER)) { "Invalid gravity value. Use START " + "| END | BOTTOM | TOP | CENTER constants" }
    }

    @Throws(IllegalStateException::class)
    override fun attachToRecyclerView(@Nullable recyclerView: RecyclerView?) {
        recyclerView?.apply {
            onFlingListener = null
            if (gravity == Gravity.START || gravity == Gravity.END) {
                isRtl = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL
            }
            this@GravitySnapHelper.recyclerView = recyclerView
        }
        super.attachToRecyclerView(recyclerView)
    }

    @Nullable
    override fun findSnapView(@NonNull lm: RecyclerView.LayoutManager): View? {
        return findSnapView(lm, true)
    }

    @Nullable
    fun findSnapView(@NonNull lm: RecyclerView.LayoutManager, checkEdgeOfList: Boolean): View? {
        var snapView: View? = null

        when (gravity) {
            Gravity.START -> snapView = findView(lm, getHorizontalHelper(lm), Gravity.START, checkEdgeOfList)
            Gravity.END -> snapView = findView(lm, getHorizontalHelper(lm), Gravity.END, checkEdgeOfList)
            Gravity.TOP -> snapView = findView(lm, getVerticalHelper(lm), Gravity.START, checkEdgeOfList)
            Gravity.BOTTOM -> snapView = findView(lm, getVerticalHelper(lm), Gravity.END, checkEdgeOfList)
            Gravity.CENTER -> snapView = if (lm.canScrollHorizontally()) {
                findView(lm, getHorizontalHelper(lm), Gravity.CENTER,
                        checkEdgeOfList)
            } else {
                findView(lm, getVerticalHelper(lm), Gravity.CENTER,
                        checkEdgeOfList)
            }
        }
        nextSnapPosition = if (snapView != null) {
            recyclerView.getChildAdapterPosition(snapView)
        } else {
            RecyclerView.NO_POSITION
        }
        return snapView
    }

    @NonNull
    override fun calculateDistanceToFinalSnap(@NonNull layoutManager: RecyclerView.LayoutManager,
                                              @NonNull targetView: View): IntArray? {
        if (gravity == Gravity.CENTER) {

            return super.calculateDistanceToFinalSnap(layoutManager, targetView)
        }

        val out = IntArray(2)

        if (layoutManager !is LinearLayoutManager) {
            return out
        }

        if (layoutManager.canScrollHorizontally()) {
            if (isRtl && gravity == Gravity.END || !isRtl && gravity == Gravity.START) {
                out[0] = getDistanceToStart(targetView, getHorizontalHelper(layoutManager))
            } else {
                out[0] = getDistanceToEnd(targetView, getHorizontalHelper(layoutManager))
            }
        } else if (layoutManager.canScrollVertically()) {
            if (gravity == Gravity.TOP) {
                out[1] = getDistanceToStart(targetView, getVerticalHelper(layoutManager))
            } else {
                out[1] = getDistanceToEnd(targetView, getVerticalHelper(layoutManager))
            }
        }
        return out
    }

    @NonNull
    override fun calculateScrollDistance(velocityX: Int, velocityY: Int): IntArray {
        if (!this::recyclerView.isInitialized
                || !this::verticalHelper.isInitialized && !this::horizontalHelper.isInitialized
                || maxFlingDistance == FLING_DISTANCE_DISABLE && maxFlingSizeFraction == FLING_SIZE_FRACTION_DISABLE) {
            return super.calculateScrollDistance(velocityX, velocityY)
        }
        val out = IntArray(2)
        val scroller = Scroller(recyclerView.context,
                DecelerateInterpolator())
        val maxDistance = flingDistance
        scroller.fling(0, 0, velocityX, velocityY,
                -maxDistance, maxDistance,
                -maxDistance, maxDistance)
        out[0] = scroller.finalX
        out[1] = scroller.finalY
        return out
    }

    @Nullable
    override fun createScroller(layoutManager: RecyclerView.LayoutManager): RecyclerView.SmoothScroller? {
        return if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider || !this::recyclerView.isInitialized) {
            null
        } else object : LinearSmoothScroller(recyclerView.context) {
            override fun onTargetFound(targetView: View,
                                                 state: RecyclerView.State,
                                                 action: Action) {
                if (!this@GravitySnapHelper::recyclerView.isInitialized || this@GravitySnapHelper.recyclerView.layoutManager == null) {
                    // The associated RecyclerView has been removed so there is no action to take.
                    return
                }
                recyclerView.layoutManager?.let {

                    calculateDistanceToFinalSnap(it,
                            targetView)?.let {out ->
                        val dx = out[0]
                        val dy = out[1]
                        val time = calculateTimeForDeceleration(Math.max(Math.abs(dx), Math.abs(dy)))
                        if (time > 0) {
                            action.update(dx, dy, time, mDecelerateInterpolator)
                        }
                    }
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

    /**
     * Changes the gravity of this [GravitySnapHelper]
     * and dispatches a smooth scroll for the new snap position.
     *
     * @param newGravity one of the following: [Gravity.START], [Gravity.TOP],
     * [Gravity.END], [Gravity.BOTTOM], [Gravity.CENTER]
     * @param smooth     true if we should smooth scroll to new edge, false otherwise
     */
    @JvmOverloads
    fun setGravity(newGravity: Int, smooth: Boolean? = true) {
        if (this.gravity != newGravity) {
            this.gravity = newGravity
            updateSnap(smooth, false)
        }
    }

    /**
     * Updates the current view to be snapped
     *
     * @param smooth          true if we should smooth scroll, false otherwise
     * @param checkEdgeOfList true if we should check if we're at an edge of the list
     * and snap according to [GravitySnapHelper.snapLastItem],
     * or false to force snapping to the nearest view
     */
    private fun updateSnap(smooth: Boolean?, checkEdgeOfList: Boolean?) {
        recyclerView.layoutManager?.let { lm ->
            val snapView = findSnapView(lm, checkEdgeOfList!!)
            if (snapView != null) {
                calculateDistanceToFinalSnap(lm, snapView)?.let { out ->
                    if (smooth!!) {
                        recyclerView.smoothScrollBy(out[0], out[1])
                    } else {
                        recyclerView.scrollBy(out[0], out[1])
                    }
                }
            }
        }
    }

    /**
     * This method will only work if there's a ViewHolder for the given position.
     *
     * @return true if scroll was successful, false otherwise
     */
    private fun scrollToPosition(position: Int): Boolean {
        return if (position == RecyclerView.NO_POSITION) {
            false
        } else scrollTo(position, false)
    }

    /**
     * Unlike [GravitySnapHelper.scrollToPosition],
     * this method will generally always find a snap view if the position is valid.
     *
     *
     * The smooth scroller from [GravitySnapHelper.createScroller]
     * will be used, and so will [GravitySnapHelper.scrollMsPerInch] for the scroll velocity
     *
     * @return true if scroll was successful, false otherwise
     */
    fun smoothScrollToPosition(position: Int): Boolean {
        return if (position == RecyclerView.NO_POSITION) {
            false
        } else scrollTo(position, true)
    }

    /**
     * Get the current gravity being applied
     *
     * @return one of the following: [Gravity.START], [Gravity.TOP], [Gravity.END],
     * [Gravity.BOTTOM], [Gravity.CENTER]
     */
    fun getGravity(): Int {
        return this.gravity
    }

    /**
     * @return last distance set through [GravitySnapHelper.setMaxFlingDistance]
     * or [GravitySnapHelper.FLING_DISTANCE_DISABLE] if we're not limiting the fling distance
     */
    fun getMaxFlingDistance(): Int {
        return maxFlingDistance
    }

    /**
     * @return last distance set through [GravitySnapHelper.setMaxFlingSizeFraction]
     * or [GravitySnapHelper.FLING_SIZE_FRACTION_DISABLE]
     * if we're not limiting the fling distance
     */
    fun getMaxFlingSizeFraction(): Float {
        return maxFlingSizeFraction
    }

    /**
     * @return true if the scroll will snap to a view, false otherwise
     */
    private fun scrollTo(position: Int, smooth: Boolean): Boolean {
        recyclerView.layoutManager?.let { layoutManager ->
            if (smooth) {
                val smoothScroller = createScroller(layoutManager)
                if (smoothScroller != null) {
                    smoothScroller.targetPosition = position
                    layoutManager.startSmoothScroll(smoothScroller)
                    return true
                }

            } else {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                if (viewHolder != null) {
                    calculateDistanceToFinalSnap(layoutManager,
                            viewHolder.itemView)?.let { distances ->
                        recyclerView.scrollBy(distances[0], distances[1])
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun getDistanceToStart(targetView: View, @NonNull helper: OrientationHelper): Int {
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

    private fun getDistanceToEnd(targetView: View, @NonNull helper: OrientationHelper): Int {
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
    @Nullable
    private fun findView(@NonNull layoutManager: RecyclerView.LayoutManager,
                         @NonNull helper: OrientationHelper,
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
        var distanceToTarget = Integer.MAX_VALUE
        val center: Int = if (layoutManager.getClipToPadding()) {
            helper.startAfterPadding + helper.totalSpace / 2
        } else {
            helper.end / 2
        }

        val snapToStart = gravity == Gravity.START && !isRtl || gravity == Gravity.END && isRtl

        val snapToEnd = gravity == Gravity.START && isRtl || gravity == Gravity.END && !isRtl

        for (i in 0 until layoutManager.childCount) {
            val currentView = layoutManager.getChildAt(i)
            val currentViewDistance: Int
            currentViewDistance = if (snapToStart) {
                if (!snapToPadding) {
                    abs(helper.getDecoratedStart(currentView))
                } else {
                    abs(helper.startAfterPadding - helper.getDecoratedStart(currentView))
                }
            } else if (snapToEnd) {
                if (!snapToPadding) {
                    abs(helper.getDecoratedEnd(currentView) - helper.end)
                } else {
                    abs(helper.endAfterPadding - helper.getDecoratedEnd(currentView))
                }
            } else {
                abs(helper.getDecoratedStart(currentView) + helper.getDecoratedMeasurement(currentView) / 2 - center)
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
            lm.findFirstCompletelyVisibleItemPosition() == 0 || lm.findLastCompletelyVisibleItemPosition() == lm.itemCount - 1
        } else {
            lm.findFirstCompletelyVisibleItemPosition() == 0
        }
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (!this::verticalHelper.isInitialized || verticalHelper.layoutManager !== layoutManager) {
            verticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return verticalHelper
    }

    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (!this::horizontalHelper.isInitialized || horizontalHelper.layoutManager !== layoutManager) {
            horizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return horizontalHelper
    }

    companion object {

        const val FLING_DISTANCE_DISABLE = -1
        const val FLING_SIZE_FRACTION_DISABLE = -1f
    }

}