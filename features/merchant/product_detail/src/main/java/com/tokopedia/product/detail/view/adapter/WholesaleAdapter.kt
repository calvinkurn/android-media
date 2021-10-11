package com.tokopedia.product.detail.view.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Wholesale
import com.tokopedia.product.detail.common.getCurrencyFormatted

import java.util.ArrayList

/**
 * @author by alifa on 5/16/17.
 */

class WholesaleAdapter(private val context: Context) : RecyclerView.Adapter<WholesaleAdapter.WholeSaleViewHolder>() {

    private var wholesalePrices = ArrayList<Wholesale>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WholesaleAdapter.WholeSaleViewHolder {
        return WholesaleAdapter.WholeSaleViewHolder(LayoutInflater.from(context).inflate(
            R.layout.item_wholesale_detail, parent, false))
    }

    override fun onBindViewHolder(holder: WholesaleAdapter.WholeSaleViewHolder, position: Int) {
        if (position < wholesalePrices.size) {
            holder.bindData(wholesalePrices[position])
        }
    }

    override fun getItemCount(): Int {
        return wholesalePrices.size
    }

    fun setData(shopShipments: ArrayList<Wholesale>) {
        this.wholesalePrices = shopShipments
        notifyDataSetChanged()
    }

    class WholeSaleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById<View>(R.id.courier_item_name) as TextView
        var info: TextView = itemView.findViewById<View>(R.id.courier_item_info) as TextView

        fun bindData(productWholesalePrice: Wholesale) {
            name.text = productWholesalePrice.price.getCurrencyFormatted()
            info.text = itemView.context.getString(R.string.min_buy_x, productWholesalePrice.minQty)
        }
    }
}

