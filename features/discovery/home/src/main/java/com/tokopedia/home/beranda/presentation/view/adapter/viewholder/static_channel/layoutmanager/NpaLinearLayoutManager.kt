package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.layoutmanager

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.home.util.HomeServerLogger.TYPE_ERROR_ON_LAYOUT_CHILDREN_BALANCE

/**
 * No Predictive Animations LinearLayoutManager
 */
class NpaLinearLayoutManager(context: Context) : LinearLayoutManager(context, HORIZONTAL, false) {
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
        } catch (e: Exception) {
            HomeServerLogger.logWarning(
                type = TYPE_ERROR_ON_LAYOUT_CHILDREN_BALANCE,
                throwable = e,
                reason = e.message.toString()
            )
        }
    }
}
