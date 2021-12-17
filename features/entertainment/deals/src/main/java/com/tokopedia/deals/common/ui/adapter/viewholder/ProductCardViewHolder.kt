package com.tokopedia.deals.common.ui.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.ProductCardListener
import com.tokopedia.deals.common.ui.dataview.ProductCardDataView
import com.tokopedia.deals.databinding.ItemDealsProductCardBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show

class ProductCardViewHolder(itemView: View, private val productCardListener: ProductCardListener) :
    RecyclerView.ViewHolder(itemView) {

    fun bindData(productCardDataView: ProductCardDataView) {
        val binding = ItemDealsProductCardBinding.bind(itemView)
        binding.run {
            imgProductCard.loadImage(productCardDataView.imageUrl)
            productCardDataView.productCategory?.let {
                if(it.name.trim().isNotEmpty()) {
                    txtProductCardCategory.show()
                    txtProductCardCategory.text = it.name
                    txtProductCardCategory.setTextColor(ContextCompat.getColor(txtProductCardCategory.context, it.color))
                } else txtProductCardCategory.hide()
            }
            txtProductCardTitle.text = productCardDataView.title

            if (productCardDataView.discount.isNotEmpty() && !productCardDataView.discount.startsWith(ZERO_PERCENT)) {
                labelProductCardDiscount.show()
                labelProductCardDiscount.setLabel(productCardDataView.discount)
            } else {
                labelProductCardDiscount.hide()
            }

            if(productCardDataView.oldPrice.isNotEmpty() && productCardDataView.oldPrice != productCardDataView.price){
                txtProductCardOldPrice.show()
                txtProductCardOldPrice.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = productCardDataView.oldPrice
                }
            } else {
                txtProductCardOldPrice.hide()
            }



            txtProductCardPrice.text = productCardDataView.price
            txtProductCardShop.text = productCardDataView.shop

            root.setOnClickListener {
                productCardListener.onProductClicked(
                    it,
                    productCardDataView,
                    adapterPosition
                )
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_deals_product_card
        private const val ZERO_PERCENT = "0%"
    }
}