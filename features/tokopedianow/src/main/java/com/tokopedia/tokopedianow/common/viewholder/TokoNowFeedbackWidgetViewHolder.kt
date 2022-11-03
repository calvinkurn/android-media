package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowFeedbackWidgetUiModel
import com.tokopedia.tokopedianow.common.view.ProductFeedbackView

class TokoNowFeedbackWidgetViewHolder(private val itemView:View,private var listener:FeedbackWidgetListener?) : AbstractViewHolder<TokoNowFeedbackWidgetUiModel>(itemView){
    private var feedbackView:ProductFeedbackView?=null
    private var divider:View?=null
    init {
        feedbackView = itemView.findViewById(R.id.feedback_view)
        divider = itemView.findViewById(R.id.tokonow_feedback_widget_divider)
    }
    override fun bind(element: TokoNowFeedbackWidgetUiModel?) {
        feedbackView?.setupFeedbackListener(listener)
        element?.let {
            if(it.isDivider) divider?.visibility = View.VISIBLE
            else divider?.visibility = View.GONE
        }
    }

    interface FeedbackWidgetListener{
        fun onFeedbackCtaClicked(view:View)
    }

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.feedback_view_tokonow
    }
}
