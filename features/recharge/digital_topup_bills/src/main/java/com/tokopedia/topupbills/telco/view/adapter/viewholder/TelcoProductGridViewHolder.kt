package com.tokopedia.topupbills.telco.view.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.view.adapter.DigitalProductAdapter

class TelcoProductGridViewHolder(itemView: View, listener: DigitalProductAdapter.ActionListener)
    : BaseTelcoProductViewHolder(itemView, listener) {

    private val layoutProduct: LinearLayout = itemView.findViewById(R.id.layout_product)
    private val descProduct: TextView = itemView.findViewById(R.id.desc_product)
    private val productPromoPrice: TextView = itemView.findViewById(R.id.product_promo_price)
    private val productPrice: TextView = itemView.findViewById(R.id.product_price)

    init {
        layoutProduct.setOnClickListener {
            onClickProductItem()
        }
    }

    override fun bindView(products: List<TelcoProduct>, item: TelcoProduct) {
        super.bindView(products, item)
        bindViewItem(descProduct, productPromoPrice, productPrice)
        setItemSelected(layoutProduct)
    }

    companion object {
        val LAYOUT = R.layout.item_digital_product_grid
    }
}