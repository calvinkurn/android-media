package com.tokopedia.shopdiscount.subsidy.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.getPercentFormatted
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.LayoutItemShopDiscountSubsidyProductBinding
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountProductSubsidyUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ShopDiscountSubsidyProductItemViewHolder(
    private val viewBinding: LayoutItemShopDiscountSubsidyProductBinding,
    private val totalProduct: Int,
    private val listener: Listener
) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_item_shop_discount_subsidy_product
    }

    interface Listener{
        fun onClickCheckbox(isChecked: Boolean, uiModel: ShopDiscountProductSubsidyUiModel)
    }

    private val container: View = viewBinding.container
    private val checkboxProduct: CheckboxUnify = viewBinding.checkboxProduct
    private val imageProduct: ImageUnify = viewBinding.imageProduct
    private val textProductName: Typography = viewBinding.textProductName
    private val textVariantName: Typography = viewBinding.textVariantName
    private val textSubsidyStatus: Typography = viewBinding.textSubsidyStatus
    private val textDiscountedPrice: Typography = viewBinding.textProductPriceDiscounted
    private val labelDiscount: Label = viewBinding.labelDiscount
    private val textProductPriceOriginal: Typography = viewBinding.textProductPriceOriginal
    private val bottomDivider: View = viewBinding.bottomDivider

    fun bind(uiModel: ShopDiscountProductSubsidyUiModel) {
        setupCheckbox(uiModel)
        setProductData(uiModel)
        setContainerBackground()
        configBottomDivider()
        setupListener(uiModel)
    }

    private fun setupCheckbox(uiModel: ShopDiscountProductSubsidyUiModel) {
        checkboxProduct.apply {
            isChecked = uiModel.isSelected
        }
    }

    private fun setupListener(uiModel: ShopDiscountProductSubsidyUiModel) {
        viewBinding.root.setOnClickListener {
            checkboxProduct.isChecked = !checkboxProduct.isChecked
            listener.onClickCheckbox(checkboxProduct.isChecked, uiModel)
        }
    }

    private fun configBottomDivider() {
        bottomDivider.apply {
            if (bindingAdapterPosition == totalProduct - 1) {
                hide()
            } else {
                show()
            }
        }
    }

    private fun setProductData(uiModel: ShopDiscountProductSubsidyUiModel) {
        imageProduct.loadImage(uiModel.productDetailData.productImageUrl)
        textProductName.text = uiModel.productDetailData.productName
        //TODO uncomment when variant name is available
//        textVariantName.text = uiModel.productDetailData.variantName
        textVariantName.text = "VARIANT_NAME"
        textSubsidyStatus.text = uiModel.productDetailData.subsidyStatusText
        textDiscountedPrice.text = uiModel.productDetailData.maxPriceDiscounted.getCurrencyFormatted()
        labelDiscount.apply {
            text = uiModel.productDetailData.maxDiscount.getPercentFormatted()
        }
        textProductPriceOriginal?.apply {
            text = uiModel.productDetailData.maxOriginalPrice.getPercentFormatted()
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun setContainerBackground() {
        val backgroundResource = when (bindingAdapterPosition) {

            totalProduct - 1 -> {
                R.drawable.shop_discount_bg_subsidy_product_item_bottom_radius
            }

            else -> {
                    R.drawable.shop_discount_bg_subsidy_product_item_no_radius
            }
        }
        container?.setBackgroundResource(backgroundResource)
    }


}
