package com.tokopedia.cmhomewidget.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetProductCardBinding
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetActionButton
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProductCardData
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetProductCardListener

class CMHomeWidgetProductCardViewHolder(
    private val binding: LayoutCmHomeWidgetProductCardBinding,
    private val listener: CMHomeWidgetProductCardListener
) : AbstractViewHolder<CMHomeWidgetProductCardData>(binding.root) {

    override fun bind(dataItem: CMHomeWidgetProductCardData) {
        setProductImage(dataItem.productImageUrl)
        setProductName(dataItem.productName)
        setProductCurrentPrice(dataItem.productCurrentPrice)
        handleProductDiscountPrice(dataItem.productDroppedPercent, dataItem.productActualPrice)
        setShopBadgeImage(dataItem.cmHomeWidgetShop?.shopBadgeImageUrl)
        setShopName(dataItem.cmHomeWidgetShop?.shopName)
        setProductBadgeImage(dataItem.productBadgeImageUrl)
        handleCTA(dataItem.cmHomeWidgetActionButtons)
        setOnClickListeners(dataItem)
        binding.root.post {
            listener.setProductCardHeight(binding.root.measuredHeight)
        }
    }

    private fun setProductImage(productImageUrl: String?) {
        productImageUrl?.let {
            binding.ivCmHomeWidgetProduct.setImageUrl(it)
        }
    }

    private fun setProductName(productName: String?) {
        binding.tvCmHomeWidgetProductName.text = productName
    }

    private fun setProductCurrentPrice(productCurrentPrice: String?) {
        binding.tvCmHomeWidgetProductCurrentPrice.text = productCurrentPrice
    }

    private fun handleProductDiscountPrice(
        productDroppedPercent: String?,
        productActualPrice: String?
    ) {
        if (!productDroppedPercent.isNullOrBlank()) {
            binding.cmHomeWidgetDiscountGroup.visibility = View.VISIBLE
            binding.lbCmHomeWidgetProductDiscount.setLabel(productDroppedPercent)
            binding.tvCmHomeWidgetProductActualPrice.apply {
                paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = productActualPrice
            }
        } else {
            binding.cmHomeWidgetDiscountGroup.visibility = View.GONE
        }
    }

    private fun setShopBadgeImage(shopBadgeImageUrl: String?) {
        shopBadgeImageUrl?.let {
            if (shopBadgeImageUrl.isNullOrBlank())
                binding.ivCmHomeWidgetProductShop.visibility = View.GONE
            else
                binding.ivCmHomeWidgetProductShop.setImageUrl(it)
        }
    }

    private fun setShopName(shopName: String?) {
        binding.tvCmHomeWidgetProductShopName.text = shopName
    }

    private fun setProductBadgeImage(productBadgeImageUrl: String?) {
        productBadgeImageUrl?.let { badgeImageUrl ->
            if (productBadgeImageUrl.isNullOrBlank())
                binding.ivCmHomeWidgetProductBadge.visibility = View.GONE
            else
                binding.ivCmHomeWidgetProductBadge.setImageUrl(badgeImageUrl)
        }
    }

    private fun handleCTA(cmHomeWidgetActionButtons: List<CMHomeWidgetActionButton>?) {
        if (cmHomeWidgetActionButtons.isNullOrEmpty()) {
            binding.btnCmHomeWidgetProduct.visibility = View.GONE
        } else {
            binding.btnCmHomeWidgetProduct.visibility = View.VISIBLE
            binding.btnCmHomeWidgetProduct.text = cmHomeWidgetActionButtons[0].actionButtonText
        }
    }

    private fun setOnClickListeners(dataItem: CMHomeWidgetProductCardData) {
        binding.btnCmHomeWidgetProduct.setOnClickListener {
            listener.onBuyDirectBtnClick(dataItem)
        }
        binding.root.setOnClickListener {
            listener.onProductCardClick(dataItem)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_cm_home_widget_product_card
        const val RATIO_WIDTH = 0.689
    }
}