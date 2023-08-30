package com.tokopedia.home_component.util

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * No Predictive Animations LinearLayoutManager
 */
class NpaLinearLayoutManager(context: Context, direction: Int) : LinearLayoutManager(context, direction, false) {
    /**
     * Disable predictive animations. There is a bug in RecyclerView which causes views that
     * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
     * adapter size has decreased since the ViewHolder was recycled.
     */
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (_: Exception) { }
    }

    override fun onItemsChanged(recyclerView: RecyclerView) {
        try {
            super.onItemsChanged(recyclerView)
        } catch (_: Exception) { }
    }

    override fun onItemsAdded(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        try {
            super.onItemsAdded(recyclerView, positionStart, itemCount)
        } catch (_: Exception) { }
    }

    override fun onItemsRemoved(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
       try {
           super.onItemsRemoved(recyclerView, positionStart, itemCount)
       } catch (_: Exception) { }
    }

    override fun onItemsUpdated(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        try {
            super.onItemsUpdated(recyclerView, positionStart, itemCount)
        } catch (_: Exception) { }
    }

    override fun onItemsUpdated(
        recyclerView: RecyclerView,
        positionStart: Int,
        itemCount: Int,
        payload: Any?
    ) {
        try {
            super.onItemsUpdated(recyclerView, positionStart, itemCount, payload)
        } catch (_: Exception) { }
    }

    override fun onItemsMoved(recyclerView: RecyclerView, from: Int, to: Int, itemCount: Int) {
        try {
            super.onItemsMoved(recyclerView, from, to, itemCount)
        } catch (_: Exception) { }
    }
}
