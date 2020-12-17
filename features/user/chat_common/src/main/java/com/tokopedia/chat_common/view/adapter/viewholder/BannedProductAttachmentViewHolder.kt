package com.tokopedia.chat_common.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.BannedProductAttachmentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.toPx

open class BannedProductAttachmentViewHolder(itemView: View?, val listener: ProductAttachmentListener)
    : BaseChatViewHolder<BannedProductAttachmentViewModel>(itemView) {

    protected open var container: ConstraintLayout? = null
    protected open var warning: Ticker? = null
    protected open var name: TextView? = null
    protected open var price: TextView? = null
    protected open var btnBuy: UnifyButton? = null
    protected open var image: ImageView? = null

    init {
        container = itemView?.findViewById(R.id.bubble_product)
        warning = itemView?.findViewById(R.id.banned_warning)
        name = itemView?.findViewById(R.id.product_name)
        price = itemView?.findViewById(R.id.product_price)
        btnBuy = itemView?.findViewById(R.id.btn_buy)
        image = itemView?.findViewById(R.id.product_image)
    }

    override fun bind(viewModel: BannedProductAttachmentViewModel) {
        setAlignment(viewModel)
        bindWarning(viewModel)
        bindImage(viewModel)
        bindName(viewModel)
        bindPrice(viewModel)
        bindSeamlessRedirect(viewModel)
        listener.trackSeenBannedProduct(viewModel)
    }

    private fun bindSeamlessRedirect(viewModel: BannedProductAttachmentViewModel) {
        btnBuy?.setOnClickListener { listener.onClickBannedProduct(viewModel) }
    }

    private fun setAlignment(viewModel: BannedProductAttachmentViewModel) {
        if (viewModel.isSender) {
            setChatRight()
        } else {
            setChatLeft()
        }
    }

    private fun setChatRight() {
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, container)
    }

    private fun setChatLeft() {
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, container)
    }

    private fun setAlignParent(alignment: Int, view: View?) {
        if (view == null) return
        if (view.layoutParams is RelativeLayout.LayoutParams) {
            val params = view.layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
            params.addRule(alignment)
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT
            view.layoutParams = params
        }
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
            ImageHandler.loadImageRounded2(itemView.context, it, imageUrl, 8.toPx().toFloat())
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