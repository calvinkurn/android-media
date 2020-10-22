package com.tokopedia.chatbot.view.adapter.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.AttachInvoiceSentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ChatBotTimeConverter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by Hendri on 27/03/18.
 */

class AttachedInvoiceSentViewHolder(itemView: View) : BaseChatViewHolder<AttachInvoiceSentViewModel>(itemView) {

    private val productName: TextView
    private val productDesc: TextView
    private val totalAmount: TextView
    private val productInvoiceDate: TextView
    private val productStatus: TextView
    private val productImage: ImageView?

    private val chatStatus: ImageView
    private val action: ImageView

    init {
        productName = itemView.findViewById(R.id.attach_invoice_sent_item_product_name)
        productDesc = itemView.findViewById(R.id.attach_invoice_sent_item_product_desc)
        totalAmount = itemView.findViewById(R.id.attach_invoice_sent_item_invoice_total)
        productImage = itemView.findViewById(R.id.attach_invoice_sent_item_product_image)
        productInvoiceDate = itemView.findViewById(R.id.attach_invoice_item_invoice_date)
        productStatus = itemView.findViewById(R.id.attach_invoice_item_invoice_status)
        chatStatus = itemView.findViewById(R.id.chat_status)
        action = itemView.findViewById(R.id.left_action)
        hour = itemView.findViewById(hourId)
    }


    override fun bind(element: AttachInvoiceSentViewModel) {
        prerequisiteUISetup(element)
        productName.text = element.message
        productDesc.text = element.description
        totalAmount.text = String.format("Total: %s",element.totalAmount)
        productStatus.text = element.status
        productInvoiceDate.text = element.createTime
        if (!TextUtils.isEmpty(element.imageUrl)) {
            productImage?.show()
            ImageHandler.LoadImage(productImage, element.imageUrl)
        } else {
            productImage?.invisible()
        }
    }

    private fun prerequisiteUISetup(element: AttachInvoiceSentViewModel) {
        action.hide()

        val resource = if (element.isDummy)
            com.tokopedia.chat_common.R.drawable.ic_chat_pending;
        else
            if (element.isRead)
                com.tokopedia.chat_common.R.drawable.ic_chat_read
            else
                com.tokopedia.chat_common.R.drawable.ic_chat_unread
        chatStatus.setImageDrawable(MethodChecker.getDrawable(chatStatus.getContext(),resource))
        hour.text = getHourTime(element.replyTime ?: "")
    }

    override fun onViewRecycled() {
        if (productImage != null) {
            ImageHandler.clearImage(productImage)
        }
    }

    override fun getHourId(): Int {
        return R.id.hour
    }

    override fun getHourTime(replyTime: String): String {
        return ChatBotTimeConverter.getHourTime(replyTime)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.attached_invoice_sent_chat_item
    }
}
