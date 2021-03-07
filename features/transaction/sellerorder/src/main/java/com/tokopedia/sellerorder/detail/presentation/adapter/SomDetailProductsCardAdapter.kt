package com.tokopedia.sellerorder.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import kotlinx.android.synthetic.main.detail_product_card_item.view.*

/**
 * Created by fwidjaja on 2019-10-04.
 */
class SomDetailProductsCardAdapter(private val actionListener: SomDetailAdapter.ActionListener?): RecyclerView.Adapter<SomDetailProductsCardAdapter.ViewHolder>() {
    var listProducts = mutableListOf<SomDetailOrder.Data.GetSomDetail.Products>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.detail_product_card_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listProducts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            actionListener?.onClickProduct(listProducts[position].orderDetailId.toIntOrZero())
        }
        holder.itemView.iv_product.loadImage(listProducts[position].thumbnail)
        holder.itemView.tv_product_name.text = listProducts[position].name
        holder.itemView.tv_product_desc.text = StringBuilder("${listProducts[position].quantity} x ${listProducts[position].priceText}")
        if (listProducts[position].note.isNotEmpty()) {
            holder.itemView.divider_product.visibility = View.VISIBLE
            holder.itemView.tv_product_notes.visibility = View.VISIBLE
            holder.itemView.tv_product_notes.text = listProducts[position].note.replace("\\n", System.getProperty("line.separator")?: "")
        } else {
            holder.itemView.divider_product.visibility = View.GONE
            holder.itemView.tv_product_notes.visibility = View.GONE
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}