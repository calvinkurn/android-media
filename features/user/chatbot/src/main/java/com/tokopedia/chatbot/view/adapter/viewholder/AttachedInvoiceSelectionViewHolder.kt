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
import com.tokopedia.chat_common.data.OrderStatusCode
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.domain.mapper.AttachInvoiceMapper
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSelectionViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSingleViewModel
import com.tokopedia.chatbot.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label

/**
 * Created by Hendri on 28/03/18.
 */

class AttachedInvoiceSelectionViewHolder(itemView: View,
                                         private val selectedListener: AttachedInvoiceSelectionListener)
    : AbstractViewHolder<AttachInvoiceSelectionViewModel>(itemView) {
    private val singleItemAdapter: AttachedInvoiceSelectionViewHolder.AttachedInvoicesItemsAdapter
    private val invoiceSelection: RecyclerView = itemView.findViewById(R.id.attach_invoice_chat_invoice_selection)

    init {
        singleItemAdapter = AttachedInvoicesItemsAdapter()
        invoiceSelection.layoutManager = LinearLayoutManager(itemView.context,
                LinearLayoutManager.HORIZONTAL, false)
        invoiceSelection.adapter = singleItemAdapter
    }

    override fun bind(element: AttachInvoiceSelectionViewModel) {
        singleItemAdapter.setList(element.list)
    }

    private inner class AttachedInvoicesItemsAdapter : RecyclerView.Adapter<AttachedInvoiceSingleItemViewHolder>() {
        internal var list: List<AttachInvoiceSingleViewModel>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachedInvoiceSingleItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_carousel_invoice_attach, parent, false)
            return AttachedInvoiceSingleItemViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: AttachedInvoiceSingleItemViewHolder,
                                      position: Int) {
            list?.getOrNull(position)?.let { holder.bind(it) }
            holder.itemView.setOnClickListener {
                selectedListener.onInvoiceSelected(
                        AttachInvoiceMapper.invoiceViewModelToDomainInvoicePojo(list!![position])
                )
            }
        }

        override fun getItemCount(): Int {
            return list?.size ?: 0
        }

        fun getList(): List<AttachInvoiceSingleViewModel>? {
            return list
        }

        fun setList(list: List<AttachInvoiceSingleViewModel>?) {
            this.list = list
            notifyDataSetChanged()
        }
    }

    private inner class AttachedInvoiceSingleItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val invoiceDate: TextView
        private val productName: TextView
        private val productDesc: TextView
        private val invoiceStatus: Label
        private val price: TextView
        private val productImage: ImageUnify
        private val pricePrefix: TextView

        init {
            invoiceDate = itemView.findViewById(R.id.tv_invoice_date)
            productName = itemView.findViewById(R.id.tv_invoice_name)
            productDesc = itemView.findViewById(R.id.tv_invoice_desc)
            invoiceStatus = itemView.findViewById(R.id.tv_status)
            pricePrefix = itemView.findViewById(R.id.tv_price_prefix)
            price = itemView.findViewById(R.id.tv_price)
            productImage = itemView.findViewById(R.id.iv_thumbnail)
        }

        fun bind(element: AttachInvoiceSingleViewModel) {
            if (!TextUtils.isEmpty(element.imageUrl)) {
                productImage.show()
                ImageHandler.loadImageRounded2(productImage.context, productImage, element.imageUrl)
            } else {
                productImage.hide()
            }
            invoiceDate.text = element.createdTime
            productName.text = element.title
            setProductDesc(element.description)
            setStatus(element.status, element.statusId)
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
            }else{
                pricePrefix.show()
                price.text = totalAmount
                price.show()
            }
        }

        private fun setStatus(status: String, statusId: Int) {
            if (status.isNotEmpty() == true) {
                val labelType = getLabelType(statusId)
                invoiceStatus.text = status
                invoiceStatus.setLabelType(labelType)
                invoiceStatus.show()
            } else {
                invoiceStatus.invisible()
            }
        }

        private fun getLabelType(statusId: Int?): Int {
            if (statusId == null) return Label.GENERAL_DARK_GREY
            return when (OrderStatusCode.MAP[statusId]) {
                OrderStatusCode.COLOR_RED -> Label.GENERAL_LIGHT_RED
                OrderStatusCode.COLOR_GREEN -> Label.GENERAL_LIGHT_GREEN
                else -> Label.GENERAL_DARK_GREY
            }
        }

    }

    companion object {
        private val ADDITIONAL_GET_ALL_BUTTON = 1

        @LayoutRes
        val LAYOUT = R.layout.item_chat_invoice_attach_selection
    }
}
