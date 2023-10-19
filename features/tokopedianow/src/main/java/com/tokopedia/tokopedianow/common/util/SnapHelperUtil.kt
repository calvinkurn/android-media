package com.tokopedia.tokopedianow.common.util

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.tokopedianow.common.callback.SnapOnScrollCallback
import com.tokopedia.tokopedianow.common.listener.SnapPositionChangeListener

object SnapHelperUtil {
    fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
        val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        return layoutManager.getPosition(snapView)
    }

    fun RecyclerView.attachSnapHelperWithListener(
        snapHelper: SnapHelper,
        behavior: SnapOnScrollCallback.Behavior = SnapOnScrollCallback.Behavior.NOTIFY_ON_SCROLL,
        listener: SnapPositionChangeListener? = null
    ) {
        snapHelper.attachToRecyclerView(this)
        val snapOnScrollListener = SnapOnScrollCallback(
            snapHelper = snapHelper,
            listener = listener,
            behavior = behavior
        )
        addOnScrollListener(snapOnScrollListener)
    }
}
