package com.tokopedia.atc_variant.view.viewholder

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.atc_variant.R
import com.tokopedia.atc_variant.view.CheckoutVariantActionListener
import com.tokopedia.atc_variant.view.MAX_QUANTITY
import com.tokopedia.atc_variant.view.viewmodel.ProductChild
import com.tokopedia.atc_variant.view.viewmodel.ProductViewModel
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_product_detail_product_page.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class ProductViewHolder(val view: View, val listener: CheckoutVariantActionListener) : AbstractViewHolder<ProductViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_product_detail_product_page
    }

    override fun bind(element: ProductViewModel?) {
        if (element != null) {
            var stockWording = ""
            if (element.isFreeOngkir && element.freeOngkirImg.isNotBlank()) {
                itemView.img_free_ongkir.visible()
                itemView.img_free_ongkir.loadImageRounded(element.freeOngkirImg)
            } else {
                itemView.img_free_ongkir.gone()
            }

            ImageHandler.loadImageRounded2(itemView.context, itemView.img_product, element.productImageUrl)
            if (element.productChildrenList.isNotEmpty()) {
                for (productChild: ProductChild in element.productChildrenList) {
                    if (productChild.isSelected) {
                        if (productChild.isAvailable && productChild.stock == 0) {
                            element.maxOrderQuantity = MAX_QUANTITY
                        } else {
                            element.maxOrderQuantity = productChild.maxOrder
                        }
                        itemView.tv_product_name.text = productChild.productName
                        itemView.tv_product_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(productChild.productPrice, false)
                        stockWording = productChild.stockWording
                        break
                    }
                }
            } else {
                itemView.tv_product_name.text = element.productName
                itemView.tv_product_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.productPrice, false)
                if (element.discountedPercentage > 0f) {
                    itemView.tv_discount.text = String.format(getString(R.string.label_discount_percentage), element.discountedPercentage.toInt())
                    itemView.tv_discount.visible()
                } else {
                    itemView.tv_discount.hide()
                }

                if (element.originalPrice > 0 && element.originalPrice > element.productPrice) {
                    itemView.tv_original_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.originalPrice.toDouble(), false)
                    itemView.tv_original_price.paintFlags = itemView.tv_original_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    itemView.tv_original_price.visible()
                } else {
                    itemView.tv_original_price.hide()
                }
                if (element.maxOrderQuantity > 0) stockWording = itemView.context.getString(R.string.label_stock_available)
            }

            listener.onBindProductUpdateQuantityViewModel(element, stockWording)
        }
    }

}