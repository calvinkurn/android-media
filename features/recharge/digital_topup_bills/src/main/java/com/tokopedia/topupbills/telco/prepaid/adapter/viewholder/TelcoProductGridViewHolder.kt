package com.tokopedia.topupbills.telco.prepaid.adapter.viewholder

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.common.getColorFromResources
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.prepaid.adapter.DigitalProductAdapter
import com.tokopedia.unifycomponents.Label

class TelcoProductGridViewHolder(itemView: View, listener: DigitalProductAdapter.ActionListener)
    : BaseTelcoProductViewHolder(itemView, listener) {

    private val titleProduct: TextView = itemView.findViewById(R.id.title_product)
    private val layoutProduct: RelativeLayout = itemView.findViewById(R.id.layout_product)
    private val productPromoPrice: TextView = itemView.findViewById(R.id.product_promo_price)
    private val productPrice: TextView = itemView.findViewById(R.id.product_price)
    private val productLabel: Label = itemView.findViewById(R.id.label_product)
    private lateinit var product: TelcoProduct

    init {
        layoutProduct.setOnClickListener {
            onClickProductItem()
        }
    }

    override fun bindView(products: List<TelcoProduct>, item: TelcoProduct) {
        super.bindView(products, item)
        product = item
        titleProduct.text = item.attributes.desc

        renderTextColor()
        renderPrice(productPromoPrice, productPrice)
        renderLabel(productLabel)
        setItemSelected(layoutProduct)
        renderOutOfStockProduct(layoutProduct, productLabel)
    }

    private fun renderTextColor() {
        if (isProductOutOfStock()) {
            titleProduct.setTextColor(itemView.context.resources.getColorFromResources(itemView.context, com.tokopedia.unifyprinciples.R.color.light_N700_44))
            productPrice.setTextColor(itemView.context.resources.getColorFromResources(itemView.context, com.tokopedia.unifyprinciples.R.color.light_N700_44))
        } else {
            titleProduct.setTextColor(itemView.context.resources.getColorFromResources(itemView.context,com.tokopedia.unifyprinciples.R.color.light_N700_96))
            productPrice.setTextColor(itemView.context.resources.getColorFromResources(itemView.context,R.color.digital_orange_price))
        }
    }

    companion object {
        val LAYOUT = R.layout.item_digital_product_grid
    }
}