package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TokoNowFeedbackWidgetUiModel
import com.tokopedia.tokopedianow.databinding.FeedbackViewTokonowBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowFeedbackWidgetViewHolder(itemView:View,private var listener: FeedbackWidgetListener?) : AbstractViewHolder<TokoNowFeedbackWidgetUiModel>(itemView){

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.feedback_view_tokonow
    }
    private var binding:FeedbackViewTokonowBinding? by viewBinding()

    override fun bind(element: TokoNowFeedbackWidgetUiModel?) {
        binding?.feedbackView?.setupFeedbackListener(listener)
        element?.let {
            if(it.isDivider) binding?.tokonowFeedbackWidgetDivider?.visibility = View.VISIBLE
            else binding?.tokonowFeedbackWidgetDivider?.visibility = View.GONE
        }
    }

    interface FeedbackWidgetListener{
        fun onFeedbackCtaClicked()
    }
}
