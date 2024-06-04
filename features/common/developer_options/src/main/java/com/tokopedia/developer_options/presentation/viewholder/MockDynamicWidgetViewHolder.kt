package com.tokopedia.developer_options.presentation.viewholder

import android.content.Intent
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.mock_dynamic_widget.MockDynamicWidgetActivity
import com.tokopedia.developer_options.presentation.model.MockDynamicWidgetUiModel
import com.tokopedia.developer_options.tracker.DevOpsTracker
import com.tokopedia.developer_options.tracker.DevopsFeature
import com.tokopedia.unifycomponents.UnifyButton

class MockDynamicWidgetViewHolder(
    itemView: View
) : AbstractViewHolder<MockDynamicWidgetUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dev_opt_mock_dynamic_widget
    }

    override fun bind(element: MockDynamicWidgetUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.mock_dynamic_widget_btn)
        btn.setOnClickListener {
            itemView.context.apply {
                DevOpsTracker.trackEntryEvent(DevopsFeature.MOCK_DYNAMIC_WIDGET)
                val intent = Intent(this, MockDynamicWidgetActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
