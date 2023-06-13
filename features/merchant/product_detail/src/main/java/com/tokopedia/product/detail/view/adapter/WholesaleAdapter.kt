package com.tokopedia.product.detail.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Wholesale
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.unifyprinciples.Typography
import java.util.ArrayList

/**
 * @author by alifa on 5/16/17.
 */

class WholesaleAdapter : RecyclerView.Adapter<WholesaleAdapter.WholeSaleViewHolder>() {

    private var wholesalePrices = ArrayList<Wholesale>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WholeSaleViewHolder {
        return WholeSaleViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_wholesale_detail, parent, false))
    }

    override fun onBindViewHolder(holder: WholeSaleViewHolder, position: Int) {
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
        var name: Typography = itemView.findViewById(R.id.courier_item_name)
        var info: Typography = itemView.findViewById(R.id.courier_item_info)

        fun bindData(productWholesalePrice: Wholesale) {
            name.text = productWholesalePrice.price.getCurrencyFormatted()
            info.text = itemView.context.getString(R.string.min_buy_x, productWholesalePrice.minQty)
        }
    }
}

