package com.tokopedia.cmhomewidget.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetProductCardBinding
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProductCardData
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetProductCardListener

class CMHomeWidgetProductCardViewHolder(
    private val binding: LayoutCmHomeWidgetProductCardBinding,
    private val listener: CMHomeWidgetProductCardListener
) : AbstractViewHolder<CMHomeWidgetProductCardData>(binding.root) {
    override fun bind(dataItem: CMHomeWidgetProductCardData) {
        with(binding)
        {
            dataItem.imageUrl?.let { productImageUrl ->
                ivCmHomeWidgetProduct.setImageUrl(productImageUrl)
            }
            tvCmHomeWidgetProductName.text = dataItem.name.toString()
            tvCmHomeWidgetProductCurrentPrice.text = dataItem.currentPrice.toString()
            if (!dataItem.droppedPercent.isNullOrBlank()) {
                cmHomeWidgetDiscountGroup.visibility = View.VISIBLE
                lbCmHomeWidgetProductDiscount.setLabel(dataItem.droppedPercent)
                tvCmHomeWidgetProductActualPrice.apply {
                    paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = dataItem.actualPrice.toString()
                }
            } else {
                cmHomeWidgetDiscountGroup.visibility = View.GONE
            }
            dataItem.cmHomeWidgetShop?.badgeImageUrl?.let { badgeImageUrl ->
                ivCmHomeWidgetProductShop.setImageUrl(badgeImageUrl)
            }
            tvCmHomeWidgetProductShopName.text = dataItem.cmHomeWidgetShop?.name
            dataItem.badgeImageUrl?.let { badgeImageUrl ->
                ivCmHomeWidgetProductBadge.setImageUrl(badgeImageUrl)
            }
            btnCmHomeWidgetProduct.text = dataItem.cmHomeWidgetActionButtons?.get(0)?.text
            btnCmHomeWidgetProduct.setOnClickListener {
                listener.onBuyDirectBtnClick(dataItem)
            }
            root.setOnClickListener {
                listener.onProductCardClick(dataItem)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_cm_home_widget_product_card
    }
}