package com.tokopedia.chatbot.view.adapter.viewholder

import android.content.Context
import android.support.annotation.LayoutRes
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel

/**
 * Created by Hendri on 27/03/18.
 */

class AttachedInvoiceSentViewHolder(itemView: View) : BaseChatViewHolder<AttachInvoiceSentViewModel>(itemView) {

    private val productName: TextView
    private val productDesc: TextView
    private val totalAmount: TextView
    private val productImage: ImageView?

    private val chatStatus: ImageView
    private val action: ImageView

    init {
        productName = itemView.findViewById(R.id.attach_invoice_sent_item_product_name)
        productDesc = itemView.findViewById(R.id.attach_invoice_sent_item_product_desc)
        totalAmount = itemView.findViewById(R.id.attach_invoice_sent_item_invoice_total)
        productImage = itemView.findViewById(R.id.attach_invoice_sent_item_product_image)
        chatStatus = itemView.findViewById(R.id.chat_status)
        action = itemView.findViewById(R.id.left_action)
    }


    override fun bind(element: AttachInvoiceSentViewModel) {
        prerequisiteUISetup(element)
        productName.text = element.message
        productDesc.text = element.description
        totalAmount.text = element.totalAmount
        if (!TextUtils.isEmpty(element.imageUrl)) {
            productImage!!.visibility = View.VISIBLE
            ImageHandler.LoadImage(productImage, element.imageUrl)
        } else {
            productImage!!.visibility = View.GONE
        }
    }

    private fun prerequisiteUISetup(element: AttachInvoiceSentViewModel) {
        action.visibility = View.GONE

        chatStatus.setImageResource(if (element.isDummy)
            R.drawable.ic_chat_pending
        else
            if (element.isRead)
                R.drawable.ic_chat_read
            else
                R.drawable.ic_chat_unread)
    }

    override fun onViewRecycled() {
        if (productImage != null) {
            ImageHandler.clearImage(productImage)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.attached_invoice_sent_chat_item
    }
}
