package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.journeydebugger.JourneyLogger
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.ViewJourneyLogUiModel
import com.tokopedia.unifycomponents.UnifyButton

class ViewJourneyLogViewHolder(
    itemView: View
): AbstractViewHolder<ViewJourneyLogUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_view_journey_log
    }

    override fun bind(element: ViewJourneyLogUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.view_journey_log_btn)
        btn.setOnClickListener {
            JourneyLogger.getInstance(itemView.context).openActivity()
        }
    }
}
