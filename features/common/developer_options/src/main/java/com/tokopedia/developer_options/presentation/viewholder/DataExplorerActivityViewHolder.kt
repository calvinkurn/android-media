package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.DataExplorerActivityUiModel
import com.tokopedia.developer_options.tracker.DevOpsTracker
import com.tokopedia.developer_options.tracker.DevopsFeature
import com.tokopedia.unifycomponents.UnifyButton

class DataExplorerActivityViewHolder(
    itemView: View
) : AbstractViewHolder<DataExplorerActivityUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_data_explorer_activity
    }

    override fun bind(element: DataExplorerActivityUiModel?) {
        val btn = itemView.findViewById<UnifyButton>(R.id.data_explorer_btn)
        btn.setOnClickListener {
            DevOpsTracker.trackEntryEvent(DevopsFeature.VIEW_DATA_EXPLORER)
            RouteManager.route(itemView.context, ApplinkConstInternalGlobal.DATA_EXPLORER)
        }
    }
}
