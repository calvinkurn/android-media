package com.tokopedia.chatbot.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.domain.mapper.AttachInvoiceMapper
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSelectionViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSingleViewModel
import com.tokopedia.chatbot.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener

/**
 * Created by Hendri on 28/03/18.
 */

class AttachedInvoiceSelectionViewHolder(itemView: View,
                                         private val selectedListener: AttachedInvoiceSelectionListener)
    : AbstractViewHolder<AttachInvoiceSelectionViewModel>(itemView) {
    private val singleItemAdapter: AttachedInvoiceSelectionViewHolder.AttachedInvoicesItemsAdapter

    init {
        singleItemAdapter = AttachedInvoicesItemsAdapter()
        val invoiceSelection = itemView.findViewById<RecyclerView>(R.id.attach_invoice_chat_invoice_selection)
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
            if (position < list!!.size) {
                holder.bind(list!![position])
                holder.itemView.setOnClickListener { v ->
                    selectedListener.onInvoiceSelected(
                            AttachInvoiceMapper.invoiceViewModelToDomainInvoicePojo(list!![position])
                    )
                }
            } else if (position == list!!.size) {
                holder.bind(AttachInvoiceSingleViewModel(true))
            }
        }

        override fun getItemCount(): Int {
            return if (list == null) 0 else list!!.size + ADDITIONAL_GET_ALL_BUTTON
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

        private val invoiceNo: TextView
        private val invoiceDate: TextView
        private val productName: TextView
        private val productDesc: TextView
        private val invoiceStatus: TextView
        private val totalAmount: TextView
        private val productImage: ImageView
        private val invoiceContainer: View
        private val buttonContainer: View
        private val searchAllButton: ImageView

        init {
            invoiceNo = itemView.findViewById(R.id.attach_invoice_item_invoice_no)
            invoiceDate = itemView.findViewById(R.id.attach_invoice_item_invoice_date)
            productName = itemView.findViewById(R.id.attach_invoice_item_product_name)
            productDesc = itemView.findViewById(R.id.attach_invoice_item_product_desc)
            invoiceStatus = itemView.findViewById(R.id.attach_invoice_item_invoice_status)
            totalAmount = itemView.findViewById(R.id.attach_invoice_item_invoice_total)
            productImage = itemView.findViewById(R.id.attach_invoice_item_product_image)
            invoiceContainer = itemView.findViewById(R.id.container_all_invoice_attach)
            searchAllButton = itemView.findViewById(R.id.all_invoice_button)
            buttonContainer = itemView.findViewById(R.id
                    .container_invoice_attach_get_all_invoice_button)
            searchAllButton.setOnClickListener { v -> selectedListener.showSearchInvoiceScreen() }
        }

        fun bind(element: AttachInvoiceSingleViewModel) {
            if (element.isSearchAllButton) {
                invoiceContainer.visibility = View.GONE
                buttonContainer.visibility = View.VISIBLE
            } else {
                invoiceContainer.visibility = View.VISIBLE
                buttonContainer.visibility = View.GONE
                invoiceNo.text = element.code
                if (!TextUtils.isEmpty(element.imageUrl)) {
                    productImage.visibility = View.VISIBLE
                    ImageHandler.loadImageAndCache(productImage, element.imageUrl)
                } else {
                    productImage.visibility = View.GONE
                }
                invoiceDate.text = element.createdTime
                productName.text = element.title
                productDesc.text = element.description
                invoiceStatus.text = element.status
                totalAmount.text = element.amount
            }
        }
    }

    companion object {
        private val ADDITIONAL_GET_ALL_BUTTON = 1

        @LayoutRes
        val LAYOUT = R.layout.item_chat_invoice_attach_selection
    }
}
