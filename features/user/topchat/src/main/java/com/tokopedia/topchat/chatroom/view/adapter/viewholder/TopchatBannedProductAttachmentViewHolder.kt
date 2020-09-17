package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.Gravity
import android.view.View
import com.tokopedia.chat_common.data.BannedProductAttachmentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BannedProductAttachmentViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.common.util.ViewUtil

class TopchatBannedProductAttachmentViewHolder(
        itemView: View?, listener: ProductAttachmentListener
) : BannedProductAttachmentViewHolder(itemView, listener) {

    private val bgOpposite = ViewUtil.generateBackgroundWithShadow(
            itemView,
            com.tokopedia.unifyprinciples.R.color.Neutral_N0,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            R.color.topchat_message_shadow,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER
    )
    private val bgSender = ViewUtil.generateBackgroundWithShadow(
            itemView,
            com.tokopedia.unifyprinciples.R.color.Neutral_N0,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            R.color.topchat_message_shadow,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER,
            R.color.bg_topchat_right_message,
            R.dimen.dp_topchat_1point5
    )

    private val padding = itemView?.context?.resources?.getDimension(R.dimen.dp_topchat_12)?.toInt()
            ?: 0

    override fun bind(viewModel: BannedProductAttachmentViewModel) {
        super.bind(viewModel)
        bindBackground(viewModel)
    }

    private fun bindBackground(viewModel: BannedProductAttachmentViewModel) {
        if (viewModel.isSender) {
            container?.background = bgSender
        } else {
            container?.background = bgOpposite
        }
        container?.setPadding(padding, padding, padding, padding)
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_banned_attached_product_chat
    }
}