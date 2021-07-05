package com.tokopedia.play.widget.ui.snaphelper

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by jegul on 14/10/20
 */
class PlayWidgetSnapHelper(
        context: Context
) : LinearSnapHelper() {

    private val offset16 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    private lateinit var orientationHelper: OrientationHelper

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
        val helper = getOrientationHelper(layoutManager)
        val endTargetPos = helper.startAfterPadding + offset16
        val itemEnd = helper.getDecoratedEnd(targetView)
        val thresholdWidth = helper.getDecoratedMeasurement(targetView) * 0.3

        val snapDistance = if (itemEnd - endTargetPos > thresholdWidth) {
            val itemStart = helper.getDecoratedStart(targetView)
            itemStart - endTargetPos
        } else itemEnd - endTargetPos
        return intArrayOf(snapDistance, 0)
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        if (layoutManager is LinearLayoutManager) {

            val isLastItemCompletelyVisible = layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

            return when {
                isLastItemCompletelyVisible -> null
                firstVisibleItem == RecyclerView.NO_POSITION -> null
                else -> layoutManager.findViewByPosition(firstVisibleItem)
            }
        }

        return super.findSnapView(layoutManager);
    }

    private fun getOrientationHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (!::orientationHelper.isInitialized || orientationHelper.layoutManager != layoutManager) {
            orientationHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return orientationHelper
    }
}