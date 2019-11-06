package com.tokopedia.chat_common.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.BannedProductAttachmentViewModel
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.unifycomponents.ticker.Ticker

class BannedProductAttachmentViewHolder(itemView: View?)
    : BaseChatViewHolder<BannedProductAttachmentViewModel>(itemView) {

    private var warning: Ticker? = null
    private var name: TextView? = null
    private var price: TextView? = null
    private var btnBuy: ButtonCompat? = null
    private var image: SquareImageView? = null


    init {
        warning = itemView?.findViewById(R.id.banned_warning)
        name = itemView?.findViewById(R.id.product_name)
        price = itemView?.findViewById(R.id.product_price)
        btnBuy = itemView?.findViewById(R.id.btn_buy)
        image = itemView?.findViewById(R.id.product_image)
    }

    override fun bind(viewModel: BannedProductAttachmentViewModel?) {
        if (viewModel == null) return
        bindWarning(viewModel)
        bindImage(viewModel)
        bindName(viewModel)
        bindPrice(viewModel)
    }

    private fun bindWarning(viewModel: BannedProductAttachmentViewModel) {
        val message = viewModel.getBannedWarningMessage()
        warning?.setTextDescription(message)

        // Workaround for ticker not wrapping multiline content correctly
        warning?.post {
            warning?.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            warning?.requestLayout()
        }
    }

    private fun bindImage(viewModel: BannedProductAttachmentViewModel) {
        image?.let {
            val imageUrl = viewModel.productImage
            ImageHandler.loadImageRounded2(itemView.context, it, imageUrl)
        }
    }

    private fun bindName(viewModel: BannedProductAttachmentViewModel) {
        val productName = viewModel.productName
        name?.text = productName
    }

    private fun bindPrice(viewModel: BannedProductAttachmentViewModel) {
        val productPrice = viewModel.productPrice
        price?.text = productPrice
    }

    companion object {
        val LAYOUT = R.layout.banned_attached_product_chat_item
    }

}