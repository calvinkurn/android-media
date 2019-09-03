package com.tokopedia.sellerorder.list.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.paintColor
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.list.data.model.SomListOrder
import kotlinx.android.synthetic.main.som_list_item.view.*

/**
 * Created by fwidjaja on 2019-08-26.
 */
class SomListItemAdapter : RecyclerView.Adapter<SomListItemAdapter.ViewHolder>() {
    var somItemList = mutableListOf<SomListOrder.Data.OrderList.Order>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.som_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return somItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.label_status_order.text = somItemList[position].status
        holder.itemView.label_invoice.text = somItemList[position].orderResi
        holder.itemView.ic_product.loadImage(somItemList[position].listOrderProduct[0].pictureUrl, com.tokopedia.design.R.drawable.ic_loading_image)
        holder.itemView.label_date_order.text = somItemList[position].orderDate
        holder.itemView.label_buyer_name.text = somItemList[position].buyerName
        holder.itemView.label_due_response_value.text = somItemList[position].deadlineText
        holder.itemView.ic_time.setColorFilter(Color.WHITE)
        holder.itemView.ic_label_due_card.setCardBackgroundColor(Color.parseColor(somItemList[position].deadlineColor))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}