package com.tokopedia.topupbills.telco.view.adapter.viewholder

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.view.adapter.DigitalProductAdapter

abstract class BaseTelcoProductViewHolder(itemView: View, val listener: DigitalProductAdapter.ActionListener) : RecyclerView.ViewHolder(itemView) {

    protected val titleProduct: TextView = itemView.findViewById(R.id.title_product)
    private lateinit var productItem: TelcoProduct
    private lateinit var productList: List<TelcoProduct>

    open fun bindView(products: List<TelcoProduct>, item: TelcoProduct) {
        productItem = item
        productList = products
    }

    fun bindViewItem(descProduct: TextView, productPromoPrice: TextView, productPrice: TextView) {
        titleProduct.text = productItem.attributes.desc
        descProduct.text = productItem.attributes.detail
        productPrice.text = productItem.attributes.price

        productPromoPrice.visibility = View.INVISIBLE
        productItem.attributes.productPromo?.run {
            if (this.newPrice.isNotEmpty()) {
                productPrice.text = this.newPrice
                productPromoPrice.text = productItem.attributes.price
                productPromoPrice.paintFlags = productPromoPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                productPromoPrice.visibility = View.VISIBLE
            }
        }
    }

    open fun onClickProductItem() {
        for (i in productList.indices) {
            if (productList[i].attributes.selected) {
                productList[i].attributes.selected = false
                listener.notifyItemChanged(i)
                break
            }
        }

        productItem.attributes.selected = true
        listener.notifyItemChanged(adapterPosition)
    }

    open fun setItemSelected(viewGrup: ViewGroup) {
        var drawable = AppCompatResources.getDrawable(itemView.context, com.tokopedia.common.topupbills.R.drawable.common_topup_bg_transparent_round)
        if (productItem.attributes.selected) {
            listener.onClickItemProduct(productItem, adapterPosition)
            drawable = AppCompatResources.getDrawable(itemView.context, com.tokopedia.common.topupbills.R.drawable.common_topup_bg_green_light_rounded)
        }
        viewGrup.background = drawable
    }
}