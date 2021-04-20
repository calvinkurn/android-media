package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chat_common.data.BannedProductAttachmentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BannedProductAttachmentViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getStrokeWidthSenderDimenRes
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker

class TopchatBannedProductAttachmentViewHolder(
        itemView: View?, listener: ProductAttachmentListener
) : BannedProductAttachmentViewHolder(itemView, listener) {

    override var container: ConstraintLayout? = itemView?.findViewById(R.id.bubble_product)
    override var warning: Ticker? = itemView?.findViewById(R.id.banned_warning)
    override var name: TextView? = itemView?.findViewById(R.id.product_name)
    override var price: TextView? = itemView?.findViewById(R.id.product_price)
    override var btnBuy: UnifyButton? = itemView?.findViewById(R.id.btn_buy)
    override var image: ImageView? = itemView?.findViewById(R.id.product_image)

    private val bgOpposite = ViewUtil.generateBackgroundWithShadow(
            container,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER
    )
    private val bgSender = ViewUtil.generateBackgroundWithShadow(
            container,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER,
            com.tokopedia.unifyprinciples.R.color.Unify_G200,
            getStrokeWidthSenderDimenRes()
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