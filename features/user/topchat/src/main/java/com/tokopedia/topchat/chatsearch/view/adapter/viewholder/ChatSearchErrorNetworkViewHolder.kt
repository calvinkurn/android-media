package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topchat.R

class ChatSearchErrorNetworkViewHolder(itemView: View?) : AbstractViewHolder<ErrorNetworkModel>(itemView) {

    private val ivIcon: ImageView? = itemView?.findViewById(R.id.iv_icon)
    private val tvMessage: TextView? = itemView?.findViewById(R.id.message_retry)
    private val tvSubMessage: TextView? = itemView?.findViewById(R.id.sub_message_retry)
    private val tvRetryButton: TextView? = itemView?.findViewById(R.id.button_retry)

    override fun bind(errorNetworkModel: ErrorNetworkModel) {
        bindIcon(errorNetworkModel)
        bindTitleMessage(errorNetworkModel)
        bindSubErrorMessage(errorNetworkModel)
        bindCtaClick(errorNetworkModel)
    }

    private fun bindIcon(errorNetworkModel: ErrorNetworkModel) {
        if (errorNetworkModel.iconDrawableRes != 0) {
            ivIcon?.setImageDrawable(MethodChecker.getDrawable(itemView.context, errorNetworkModel.iconDrawableRes))
        }
    }

    private fun bindTitleMessage(errorNetworkModel: ErrorNetworkModel) {
        if (errorNetworkModel.errorMessage != null && errorNetworkModel.errorMessage.isNotEmpty()) {
            tvMessage?.text = errorNetworkModel.errorMessage
        }
    }

    private fun bindSubErrorMessage(errorNetworkModel: ErrorNetworkModel) {
        if (errorNetworkModel.subErrorMessage != null && errorNetworkModel.subErrorMessage.isNotEmpty()) {
            tvSubMessage?.text = errorNetworkModel.subErrorMessage
        }
    }

    private fun bindCtaClick(errorNetworkModel: ErrorNetworkModel) {
        tvRetryButton?.setOnClickListener {
            val listener: ErrorNetworkModel.OnRetryListener? = errorNetworkModel.onRetryListener
            listener?.onRetryClicked()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_error_network_chat_search
    }
}