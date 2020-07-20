package com.tokopedia.topupbills.telco.prepaid.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.common.getColorFromResources
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.prepaid.adapter.TelcoProductAdapter
import com.tokopedia.unifycomponents.Label
import kotlinx.android.synthetic.main.item_telco_product.view.*

class TelcoProductViewHolder(itemView: View, private val productType: Int,
                             val listener: OnClickListener)
    : AbstractViewHolder<TelcoProduct>(itemView) {

    lateinit var adapter: TelcoProductAdapter

    override fun bind(element: TelcoProduct) {
        with(itemView) {
            title_product.text = element.attributes.desc

            renderDescProduct(element)
            renderSeeMoreBtn(element)
            renderTextColor(element.attributes.status)
            renderPrice(element)
            renderCornerLabel(element)
            setItemSelected(element)
            renderOutOfStockProduct(element.attributes.status)
        }
    }

    private fun renderDescProduct(element: TelcoProduct) {
        with(itemView) {
            if (productType == TelcoProductType.PRODUCT_LIST) {
                telco_empty_view.hide()
                telco_desc_product.show()
                telco_desc_product.text = element.attributes.detail
            } else {
                telco_empty_view.show()
                telco_desc_product.hide()
            }
        }
    }

    private fun renderSeeMoreBtn(element: TelcoProduct) {
        with(itemView) {
            if (productType == TelcoProductType.PRODUCT_GRID) {
                telco_see_more_btn.hide()
            } else {
                telco_see_more_btn.show()
            }
            telco_see_more_btn.setOnClickListener {
                listener.onClickSeeMoreProduct(element, adapterPosition)
            }
        }
    }

    private fun renderTextColor(status: Int) {
        with(itemView) {
            if (isProductOutOfStock(status)) {
                title_product.setTextColor(itemView.context.resources.getColorFromResources(itemView.context, com.tokopedia.unifyprinciples.R.color.light_N700_44))
                telco_product_price.setTextColor(itemView.context.resources.getColorFromResources(itemView.context, com.tokopedia.unifyprinciples.R.color.light_N700_44))
            } else {
                title_product.setTextColor(itemView.context.resources.getColorFromResources(itemView.context, com.tokopedia.unifyprinciples.R.color.light_N700_96))
                telco_product_price.setTextColor(itemView.context.resources.getColorFromResources(itemView.context, R.color.digital_orange_price))
            }
        }
    }

    private fun renderPrice(element: TelcoProduct) {
        with(itemView) {
            telco_product_price.text = element.attributes.price
            telco_product_promo_price.visibility = View.INVISIBLE
            element.attributes.productPromo?.run {
                if (this.newPrice.isNotEmpty()) {
                    telco_product_price.text = this.newPrice
                    telco_product_promo_price.text = element.attributes.price
                    telco_product_promo_price.paintFlags = telco_product_promo_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    telco_product_promo_price.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun renderCornerLabel(element: TelcoProduct) {
        with(itemView) {
            if (element.attributes.productLabels.isEmpty()) {
                label_product.visibility = View.GONE
            } else {
                label_product.text = element.attributes.productLabels[0]
                label_product.setLabelType(Label.GENERAL_DARK_ORANGE)
                label_product.visibility = View.VISIBLE
            }
        }
    }

    private fun setItemSelected(element: TelcoProduct) {
        with(itemView) {
            if (::adapter.isInitialized) {
                isSelected = adapterPosition == adapter.selectedPosition && !isProductOutOfStock(element.attributes.status)

                layout_product.setOnClickListener {
                    listener.onClickItemProduct(element, adapterPosition)
                }
            }
        }
    }

    private fun renderOutOfStockProduct(status: Int) {
        with(itemView) {
            if (isProductOutOfStock(status)) {
                label_product.text = itemView.context.getString(R.string.telco_label_out_of_stock)
                label_product.visibility = View.VISIBLE
                label_product.setLabelType(Label.GENERAL_DARK_GREY)
                layout_product.setBackgroundResource(0)
                layout_product.setBackgroundResource(R.drawable.digital_bg_grey_rounded)
            }
        }
    }

    private fun isProductOutOfStock(status: Int): Boolean {
        return status == PRODUCT_STATUS_OUT_OF_STOCK
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_telco_product

        const val PRODUCT_STATUS_OUT_OF_STOCK = 3
    }

    interface OnClickListener {
        fun onClickItemProduct(element: TelcoProduct, position: Int)
        fun onClickSeeMoreProduct(element: TelcoProduct, position: Int)
    }
}
