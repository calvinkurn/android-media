package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.uoh_list_item.view.*

/**
 * Created by fwidjaja on 02/07/20.
 */

class UohListItemAdapter : RecyclerView.Adapter<UohListItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var uohItemList = mutableListOf<UohListOrder.Data.UohOrders.Order>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.uoh_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return uohItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (uohItemList.isNotEmpty()) {
            holder.itemView.ic_uoh_vertical?.loadImage(uohItemList[position].metadata.verticalLogo)
            holder.itemView.tv_uoh_categories?.text = uohItemList[position].metadata.verticalLabel
            holder.itemView.tv_uoh_date?.text = uohItemList[position].metadata.paymentDateStr
            holder.itemView.label_uoh_order?.text = uohItemList[position].status

            // holder.itemView.ticker_info_inside_card?.tickerTitle = uohItemList[position].metadata.tickers
            if (uohItemList[position].metadata.products.isNotEmpty()) {
                holder.itemView.tv_uoh_product_name?.text = uohItemList[position].metadata.products.first().title
                holder.itemView.tv_uoh_product_desc?.text = uohItemList[position].metadata.products.first().inline1.label
                if (uohItemList[position].metadata.products.first().imageURL.isNotEmpty()) {
                    holder.itemView.iv_uoh_product?.loadImage(uohItemList[position].metadata.products.first().imageURL)
                } else {
                    holder.itemView.cv_uoh_product?.gone()
                }
            }

            holder.itemView.tv_uoh_total_belanja?.text = uohItemList[position].metadata.totalPrice.label
            holder.itemView.tv_uoh_total_belanja_value?.text = uohItemList[position].metadata.totalPrice.value
            if (uohItemList[position].metadata.buttons.isNotEmpty()) {
                holder.itemView.uoh_btn_action?.text = uohItemList[position].metadata.buttons[0].label
            } else {
                holder.itemView.uoh_btn_action?.gone()
            }
        }
    }

    fun addList(list: List<UohListOrder.Data.UohOrders.Order>) {
        uohItemList.clear()
        uohItemList.addAll(list)
        notifyDataSetChanged()
    }

    fun appendList(list: List<UohListOrder.Data.UohOrders.Order>) {
        uohItemList.addAll(list)
        notifyDataSetChanged()
    }
}