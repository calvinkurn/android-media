package com.tokopedia.sellerorder.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_HEADER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_PAYMENT_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_PRODUCTS_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_SHIPPING_TYPE
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailHeaderViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailPaymentsViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailProductsViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailShippingViewHolder
import com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailFragment

/**
 * Created by fwidjaja on 2019-10-03.
 */
class SomDetailAdapter : RecyclerView.Adapter<SomDetailAdapter.BaseViewHolder<*>>() {
    var listDataDetail = mutableListOf<SomDetailData>()
    private lateinit var actionListener: ActionListener

    interface ActionListener {
        fun onShowBottomSheetInfo(title: String, resIdDesc: Int)
        fun onTextCopied(label: String, str: String)
        fun onInvalidResiUpload(awbUploadUrl: String)
        fun onDialPhone(strPhoneNo: String)
        fun onShowBookingCode(bookingCode: String, bookingType: String)
    }

    companion object {
        private const val LAYOUT_HEADER = 0
        private const val LAYOUT_PRODUCTS = 1
        private const val LAYOUT_SHIPPING = 2
        private const val LAYOUT_PAYMENTS = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val context = parent.context
        return when (viewType) {
            LAYOUT_HEADER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.detail_header_item, parent, false)
                SomDetailHeaderViewHolder(view, actionListener)
            }
            LAYOUT_PRODUCTS -> {
                val view = LayoutInflater.from(context).inflate(R.layout.detail_products_item, parent, false)
                SomDetailProductsViewHolder(view)
            }
            LAYOUT_SHIPPING -> {
                val view = LayoutInflater.from(context).inflate(R.layout.detail_shipping_item, parent, false)
                SomDetailShippingViewHolder(view, actionListener)
            }
            LAYOUT_PAYMENTS -> {
                val view = LayoutInflater.from(context).inflate(R.layout.detail_payments_item, parent, false)
                SomDetailPaymentsViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return listDataDetail.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = listDataDetail[position]
        when (holder) {
            is SomDetailHeaderViewHolder -> {
                holder.bind(element, position)
            }
            is SomDetailProductsViewHolder -> {
                holder.bind(element, position)
            }
            is SomDetailShippingViewHolder -> {
                holder.bind(element, position)
            }
            is SomDetailPaymentsViewHolder -> {
                holder.bind(element, position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (listDataDetail[position].typeLayout) {
            DETAIL_HEADER_TYPE -> LAYOUT_HEADER
            DETAIL_PRODUCTS_TYPE -> LAYOUT_PRODUCTS
            DETAIL_SHIPPING_TYPE -> LAYOUT_SHIPPING
            DETAIL_PAYMENT_TYPE -> LAYOUT_PAYMENTS
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

    fun setActionListener(fragment: SomDetailFragment) {
        this.actionListener = fragment
    }
}