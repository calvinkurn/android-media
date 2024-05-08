package com.tokopedia.developer_options.presentation.viewholder

import android.content.Intent
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.MsSdkOptionActivity
import com.tokopedia.developer_options.presentation.activity.ProductDetailDevActivity
import com.tokopedia.developer_options.presentation.model.MsSdkUiModel
import com.tokopedia.developer_options.presentation.model.PdpDevUiModel
import com.tokopedia.developer_options.tracker.DevOpsTracker
import com.tokopedia.developer_options.tracker.DevopsFeature
import com.tokopedia.unifycomponents.UnifyButton

class MsSdkViewHolder(
    itemView: View
)
    : AbstractViewHolder<MsSdkUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dev_opt_mssdk_dev
    }
    override fun bind(element: MsSdkUiModel?) {
        val btn = itemView.findViewById<UnifyButton>(R.id.mssdk_dev_btn)
        btn.setOnClickListener {
            itemView.context.apply {
                DevOpsTracker.trackEntryEvent(DevopsFeature.MSSDK_DEV)
                val intent = Intent(this, MsSdkOptionActivity::class.java)
                startActivity(intent)
            }
        }
    }

}
