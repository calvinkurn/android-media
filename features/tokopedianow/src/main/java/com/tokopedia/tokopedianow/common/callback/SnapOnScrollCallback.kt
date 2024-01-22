package com.tokopedia.tokopedianow.common.callback

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.tokopedianow.common.listener.SnapPositionChangeListener
import com.tokopedia.tokopedianow.common.util.SnapHelperUtil.getSnapPosition

class SnapOnScrollCallback(
    private val snapHelper: SnapHelper,
    private val listener: SnapPositionChangeListener? = null,
    private val behavior: Behavior = Behavior.NOTIFY_ON_SCROLL
) : RecyclerView.OnScrollListener() {
    enum class Behavior {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }

    private var snapPosition = RecyclerView.NO_POSITION

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (behavior == Behavior.NOTIFY_ON_SCROLL) {
            notifySnapPositionChange(recyclerView)
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (behavior == Behavior.NOTIFY_ON_SCROLL_STATE_IDLE && newState == RecyclerView.SCROLL_STATE_IDLE) {
            notifySnapPositionChange(recyclerView)
        }
    }

    private fun notifySnapPositionChange(recyclerView: RecyclerView) {
        val position = snapHelper.getSnapPosition(recyclerView)
        val snapPositionChanged = snapPosition != position
        if (snapPositionChanged) {
            listener?.onSnapPositionChange(position)
            snapPosition = position
        }
    }
}
