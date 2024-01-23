package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.chuckerteam.chucker.api.Chucker
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.ViewNetworkLogUiModel
import com.tokopedia.developer_options.tracker.DevOpsTracker
import com.tokopedia.developer_options.tracker.DevopsFeature
import com.tokopedia.unifycomponents.UnifyButton

class ViewNetworkLogViewHolder(
    itemView: View
) : AbstractViewHolder<ViewNetworkLogUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_view_network_log
    }

    override fun bind(element: ViewNetworkLogUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.view_network_log_btn)
        btn.setOnClickListener {
            itemView.context.apply {
                DevOpsTracker.trackEntryEvent(DevopsFeature.VIEW_NETWORK_LOG)
                startActivity(Chucker.getLaunchIntent(applicationContext, Chucker.SCREEN_HTTP))
            }
        }
    }
}
