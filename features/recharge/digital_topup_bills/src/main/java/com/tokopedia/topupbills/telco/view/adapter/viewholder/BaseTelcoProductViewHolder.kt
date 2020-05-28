package com.tokopedia.topupbills.telco.view.adapter.viewholder

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.view.adapter.DigitalProductAdapter
import com.tokopedia.unifycomponents.Label

abstract class BaseTelcoProductViewHolder(itemView: View, val listener: DigitalProductAdapter.ActionListener)
    : RecyclerView.ViewHolder(itemView) {

    private lateinit var productItem: TelcoProduct
    private lateinit var productList: List<TelcoProduct>

    open fun bindView(products: List<TelcoProduct>, item: TelcoProduct) {
        productItem = item
        productList = products
    }

    protected fun renderLabel(productLabel: TextView) {
        if (productItem.attributes.productLabels.isEmpty()) {
            productLabel.visibility = View.GONE
        } else {
            productLabel.text = productItem.attributes.productLabels[0]
            productLabel.visibility = View.VISIBLE
        }
    }

    protected fun isProductOutOfStock(): Boolean {
        return productItem.attributes.status == PRODUCT_STATUS_OUT_OF_STOCK
    }

    protected fun renderPrice(productPromoPrice: TextView, productPrice: TextView) {
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

    protected fun onClickProductItem() {
        if (!isProductOutOfStock()) {
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
    }

    protected fun renderOutOfStockProduct(viewGrup: ViewGroup, productLabel: Label) {
        if (isProductOutOfStock()) {
            productLabel.text = itemView.context.getString(R.string.telco_label_out_of_stock)
            productLabel.visibility = View.VISIBLE
            productLabel.setLabelType(Label.GENERAL_DARK_GREY)
            viewGrup.setBackgroundResource(R.drawable.digital_bg_grey_rounded)
        }
    }

    protected fun setItemSelected(viewGrup: ViewGroup) {
        var drawableResources = com.tokopedia.common.topupbills.R.drawable.common_topup_bg_transparent_round
        if (productItem.attributes.selected) {
            listener.onClickItemProduct(productItem, adapterPosition)
            drawableResources = com.tokopedia.common.topupbills.R.drawable.common_topup_bg_green_light_rounded
        }
        viewGrup.setBackgroundResource(drawableResources)
    }

    companion object {
        const val PRODUCT_STATUS_OUT_OF_STOCK = 3
    }
}