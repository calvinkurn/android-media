package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.cassava.ui.MainValidatorActivity.Companion.newInstance
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.CassavaUiModel
import com.tokopedia.developer_options.tracker.DevOpsTracker
import com.tokopedia.unifycomponents.UnifyButton

class CassavaViewHolder(
    itemView: View
) : AbstractViewHolder<CassavaUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_cassava
    }

    override fun bind(element: CassavaUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.cassava_btn)
        btn.setOnClickListener {
            DevOpsTracker.trackEntryEvent(this::class.simpleName.toString())
            itemView.context.apply { startActivity(newInstance(this)) }
        }
    }
}
