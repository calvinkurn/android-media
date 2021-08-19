package com.tokopedia.buyerorder.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.bottomsheet_product_item.view.*

/**
 * Created by fwidjaja on 12/06/20.
 */
class BuyerListOfProductsBottomSheetAdapter : RecyclerView.Adapter<BuyerListOfProductsBottomSheetAdapter.ViewHolder>() {
    var listProducts = listOf<BuyerGetCancellationReasonData.Data.GetCancellationReason.OrderDetailsCancellation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_product_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listProducts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.iv_product.loadImage(listProducts[position].picture)
        holder.itemView.label_product_name.text = listProducts[position].productName
        holder.itemView.label_price.text = listProducts[position].productPrice
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}