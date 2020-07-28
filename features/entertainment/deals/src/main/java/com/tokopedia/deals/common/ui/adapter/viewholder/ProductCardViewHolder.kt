package com.tokopedia.deals.common.ui.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.ProductCardListener
import com.tokopedia.deals.common.ui.dataview.ProductCardDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_deals_product_card.view.*

class ProductCardViewHolder(itemView: View, private val productCardListener: ProductCardListener) :
    RecyclerView.ViewHolder(itemView) {

    fun bindData(productCardDataView: ProductCardDataView) {
        itemView.run {
            img_product_card.loadImage(productCardDataView.imageUrl)
            productCardDataView.productCategory?.let {
                if(it.name.trim().isNotEmpty()) {
                    txt_product_card_category.show()
                    txt_product_card_category.text = it.name
                    txt_product_card_category.setTextColor(ContextCompat.getColor(context, it.color))
                } else txt_product_card_category.hide()
            }
            txt_product_card_title.text = productCardDataView.title

            if (productCardDataView.discount.isNotEmpty()) {
                label_product_card_discount.setLabel(productCardDataView.discount)
                txt_product_card_old_price.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = productCardDataView.oldPrice
                }
            } else {
                label_product_card_discount.hide()
                txt_product_card_old_price.hide()
            }

            txt_product_card_price.text = productCardDataView.price
            txt_product_card_shop.text = productCardDataView.shop

            setOnClickListener {
                productCardListener.onProductClicked(
                    this,
                    productCardDataView,
                    adapterPosition
                )
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_deals_product_card
    }
}