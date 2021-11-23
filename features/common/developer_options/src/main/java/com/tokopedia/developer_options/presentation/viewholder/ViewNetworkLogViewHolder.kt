package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.NetworkLogOnNotificationUiModel
import com.tokopedia.developer_options.presentation.model.ViewNetworkLogUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class ViewNetworkLogViewHolder(
    itemView: View,
    private val listener: ViewNetworkLogListener
): AbstractViewHolder<ViewNetworkLogUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_view_network_log
    }

    override fun bind(element: ViewNetworkLogUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.view_network_log_btn)
        btn.text = element.text
        btn.setOnClickListener {
            listener.onClickNetworkLogBtn()
        }
    }

    interface ViewNetworkLogListener {
        fun onClickNetworkLogBtn()
    }
}