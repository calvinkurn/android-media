package com.tokopedia.chatbot.view.adapter.viewholder

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chatbot.ChatbotConstant.RENDER_INVOICE_LIST_AND_BUTTON_ACTION
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.domain.mapper.AttachInvoiceMapper
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSelectionUiModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSingleUiModel
import com.tokopedia.chatbot.databinding.ItemChatbotCarouselInvoiceAttachBinding
import com.tokopedia.chatbot.databinding.ItemChatbotInvoiceAttachSelectionBinding
import com.tokopedia.chatbot.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener
import com.tokopedia.chatbot.view.util.InvoiceStatusLabelHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by Hendri on 28/03/18.
 */

class AttachedInvoiceSelectionViewHolder(
    itemView: View,
    private val selectedListener: AttachedInvoiceSelectionListener
) :
    AbstractViewHolder<AttachInvoiceSelectionUiModel>(itemView) {
    private val view = ItemChatbotInvoiceAttachSelectionBinding.bind(itemView)
    private val singleItemAdapter: AttachedInvoiceSelectionViewHolder.AttachedInvoicesItemsAdapter
    private val invoiceSelection: RecyclerView = view.attachInvoiceChatInvoiceSelection

    init {
        singleItemAdapter = AttachedInvoicesItemsAdapter()
        invoiceSelection.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        invoiceSelection.adapter = singleItemAdapter
    }

    override fun bind(element: AttachInvoiceSelectionUiModel) {
        if (element.status == RENDER_INVOICE_LIST_AND_BUTTON_ACTION) {
            singleItemAdapter.setList(element.list)
            invoiceSelection.show()
        } else {
            invoiceSelection.hide()
        }
    }

    private inner class AttachedInvoicesItemsAdapter : RecyclerView.Adapter<AttachedInvoiceSingleItemViewHolder>() {
        internal var list: List<AttachInvoiceSingleUiModel>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachedInvoiceSingleItemViewHolder {
            val itemView = ItemChatbotCarouselInvoiceAttachBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return AttachedInvoiceSingleItemViewHolder(itemView)
        }

        override fun onBindViewHolder(
            holder: AttachedInvoiceSingleItemViewHolder,
            position: Int
        ) {
            list?.getOrNull(position)?.let { holder.bind(it) }
            holder.pilihButton.setOnClickListener {
                selectedListener.onInvoiceSelected(
                    AttachInvoiceMapper.invoiceViewModelToDomainInvoicePojo(list!![position])
                )
            }
        }

        override fun getItemCount(): Int {
            return list?.size ?: 0
        }

        fun getList(): List<AttachInvoiceSingleUiModel>? {
            return list
        }

        fun setList(list: List<AttachInvoiceSingleUiModel>?) {
            this.list = list
            notifyDataSetChanged()
        }
    }

    private inner class AttachedInvoiceSingleItemViewHolder internal constructor(itemView: ItemChatbotCarouselInvoiceAttachBinding) : RecyclerView.ViewHolder(itemView.root) {

        private val invoiceDate: TextView
        private val productName: TextView
        private val productDesc: TextView
        private val invoiceStatus: Label
        private val price: TextView
        private val productImage: ImageUnify
        private val pricePrefix: TextView
        val pilihButton: UnifyButton

        init {
            invoiceDate = itemView.containerAllInvoiceAttach.tvInvoiceDate
            productName = itemView.containerAllInvoiceAttach.tvInvoiceName
            productDesc = itemView.containerAllInvoiceAttach.tvInvoiceDesc
            invoiceStatus = itemView.containerAllInvoiceAttach.tvStatus
            pricePrefix = itemView.containerAllInvoiceAttach.tvPricePrefix
            price = itemView.containerAllInvoiceAttach.tvPrice
            productImage = itemView.containerAllInvoiceAttach.ivThumbnail
            pilihButton = itemView.containerAllInvoiceAttach.btnPilih
        }

        fun bind(element: AttachInvoiceSingleUiModel) {
            if (!TextUtils.isEmpty(element.imageUrl)) {
                productImage.show()
                ImageHandler.loadImageRounded2(productImage.context, productImage, element.imageUrl)
            } else {
                productImage.hide()
            }
            invoiceDate.text = element.createdTime
            productName.text = element.title
            setProductDesc(element.description)
            setStatus(element.status, element.color, element.statusId)
            setPrice(element.amount)
        }

        private fun setProductDesc(description: String) {
            if (description.isNotEmpty()) {
                productDesc.show()
                productDesc.text = description
            } else {
                productDesc.hide()
            }
        }

        private fun setPrice(totalAmount: String?) {
            if (totalAmount.isNullOrEmpty()) {
                pricePrefix.hide()
                price.hide()
            } else {
                pricePrefix.show()
                price.text = totalAmount
                price.show()
            }
        }

        private fun setStatus(status: String, statusColor: String?, statusId: Int) {
            if (status.isNotEmpty() == true) {
                var labelType: Int = if (statusColor != null && statusColor.isEmpty()) {
                    InvoiceStatusLabelHelper.getLabelTypeWithStatusId(statusId)
                } else {
                    InvoiceStatusLabelHelper.getLabelType(statusColor)
                }
                invoiceStatus.text = status
                invoiceStatus.setLabelType(labelType)
                invoiceStatus.show()
            } else {
                invoiceStatus.invisible()
            }
        }
    }

    companion object {
        private val ADDITIONAL_GET_ALL_BUTTON = 1

        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_invoice_attach_selection
    }
}
