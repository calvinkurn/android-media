package com.tokopedia.topupbills.telco.view.adapter.viewholder

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.view.adapter.DigitalProductAdapter

class TelcoProductListViewHolder(itemView: View, listener: DigitalProductAdapter.ActionListener)
    : BaseTelcoProductViewHolder(itemView, listener) {

    private val titleProduct: TextView = itemView.findViewById(R.id.title_product)
    private val descProduct: TextView = itemView.findViewById(R.id.desc_product)
    private val productPromoPrice: TextView = itemView.findViewById(R.id.product_promo_price)
    private val productPrice: TextView = itemView.findViewById(R.id.product_price)
    private val seeMoreBtn: TextView = itemView.findViewById(R.id.see_more_btn)
    private val layoutProduct: RelativeLayout = itemView.findViewById(R.id.layout_product)

    init {
        layoutProduct.setOnClickListener {
            onClickProductItem()
        }
    }

    override fun bindView(products: List<TelcoProduct>, item: TelcoProduct) {
        super.bindView(products, item)
        titleProduct.text = item.attributes.desc
        descProduct.text = item.attributes.detail
        renderPrice(productPromoPrice, productPrice)
        setItemSelected(layoutProduct)

        seeMoreBtn.setOnClickListener {
            listener.onClickSeeMoreProduct(item)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_digital_product_list
    }
}