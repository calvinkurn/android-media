package com.tokopedia.shopdiscount.manage_discount.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.ShopDiscountSetupProductItemLayoutBinding
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopDiscountSetupProductItemViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<ShopDiscountSetupProductUiModel.SetupProductData>(itemView) {
    private var viewBinding: ShopDiscountSetupProductItemLayoutBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_discount_setup_product_item_layout
    }

    interface Listener {
        fun onClickManageDiscount(model: ShopDiscountSetupProductUiModel.SetupProductData)
        fun onClickDeleteProduct(
            model: ShopDiscountSetupProductUiModel.SetupProductData,
            position: Int
        )
    }

    override fun bind(uiModel: ShopDiscountSetupProductUiModel.SetupProductData) {
        setProductData(uiModel)
    }

    private fun setProductData(uiModel: ShopDiscountSetupProductUiModel.SetupProductData) {
        viewBinding?.apply {
            imgProduct.loadImage(uiModel.productImageUrl)
            textProductName.text = uiModel.productName
            setLabelDiscount(labelDiscount, uiModel.mappedResultData, uiModel.productStatus)
            setLabelTotalProductVariant(
                this,
                uiModel.mappedResultData.totalVariant,
                uiModel.mappedResultData.totalDiscountedVariant
            )
            setDisplayedPrice(textDisplayedPrice, uiModel.mappedResultData, uiModel.productStatus)
            setOriginalPrice(textOriginalPrice, uiModel.mappedResultData, uiModel.productStatus)
            setTotalStockData(textTotalStock, uiModel)
            showDivider(this)
            setManageDiscountButton(buttonManageDiscount, uiModel)
            setDeleteProductButton(imageDeleteProduct, uiModel)
        }
    }

    private fun setDeleteProductButton(
        imageDeleteProduct: ImageUnify,
        uiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ) {
        imageDeleteProduct.setOnClickListener {
            listener.onClickDeleteProduct(uiModel, adapterPosition)
        }
    }

    private fun showDivider(binding: ShopDiscountSetupProductItemLayoutBinding) {
        binding.divider.shouldShowWithAction(isShouldShowDivider(binding)) {}
    }

    private fun isShouldShowDivider(binding: ShopDiscountSetupProductItemLayoutBinding): Boolean {
        return binding.textTotalStock.isVisible || binding.textErrorValidation.isVisible
    }

    private fun setTotalStockData(
        textTotalStock: Typography,
        uiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ) {
        val totalStock = uiModel.stock
        val totalLocation = uiModel.mappedResultData.totalLocation
        textTotalStock.shouldShowWithAction(!totalStock.toIntOrZero().isZero()) {
            val totalStockFormattedString =
                when {
                    shouldUseNonVariantMultiLocTotalStockFormat(uiModel.productStatus) -> {
                        String.format(
                            getString(R.string.shop_discount_manage_discount_total_stock_non_variant_multi_loc_format),
                            totalStock,
                            totalLocation
                        )
                    }
                    shouldUseVariantMultiLocTotalStockFormat(uiModel.productStatus) -> {
                        String.format(
                            getString(R.string.shop_discount_manage_discount_total_stock_non_variant_multi_loc_format),
                            totalStock
                        )
                    }
                    else -> {
                        String.format(
                            getString(R.string.shop_discount_manage_discount_total_stock_default_format),
                            totalStock
                        )
                    }
                }
            textTotalStock.text = MethodChecker.fromHtml(totalStockFormattedString)
        }
    }

    private fun shouldUseNonVariantMultiLocTotalStockFormat(
        productStatus: ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus
    ): Boolean {
        return !productStatus.isVariant && productStatus.isMultiLoc
    }

    private fun shouldUseVariantMultiLocTotalStockFormat(
        productStatus: ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus
    ): Boolean {
        return productStatus.isVariant && productStatus.isMultiLoc
    }

    private fun setManageDiscountButton(
        buttonManageDiscount: UnifyButton,
        uiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ) {
        buttonManageDiscount.apply {
            if (uiModel.productStatus.isProductDiscounted) {
                buttonType = UnifyButton.Type.ALTERNATE
                text = getString(R.string.shop_discount_manage_discount_edit_toolbar_title)
            } else {
                buttonType = UnifyButton.Type.MAIN
                text = getString(R.string.shop_discount_manage_discount_manage_toolbar_title)
            }
            setOnClickListener {
                listener.onClickManageDiscount(uiModel)
            }
        }
    }

    private fun setLabelTotalProductVariant(
        binding: ShopDiscountSetupProductItemLayoutBinding,
        totalVariant: Int,
        totalDiscountedVariant: Int
    ) {
        binding.labelTotalVariant.shouldShowWithAction(!totalVariant.isZero()) {
            binding.labelTotalVariant.apply {
                text = String.format(
                    getString(R.string.shop_discount_manage_discount_total_variant_format),
                    totalVariant.toString()
                )
            }
        }
        binding.labelTotalDiscountedVariant.shouldShowWithAction(!totalDiscountedVariant.isZero()) {
            binding.labelTotalDiscountedVariant.apply {
                text = String.format(
                    getString(R.string.shop_discount_manage_discount_total_discounted_variant_format),
                    totalVariant.toString()
                )
            }
        }
    }

    private fun setDisplayedPrice(
        textDisplayedPrice: Typography,
        uiModel: ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData,
        productStatus: ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus
    ) {
        val minDisplayedPrice: Int
        val maxDisplayedPrice: Int
        if (!productStatus.isProductDiscounted) {
            minDisplayedPrice = uiModel.minOriginalPrice
            maxDisplayedPrice = uiModel.maxOriginalPrice
        } else {
            minDisplayedPrice = uiModel.minDisplayedPrice
            maxDisplayedPrice = uiModel.maxDisplayedPrice
        }
        val formattedDisplayedPrice = getFormattedRangeString(
            minDisplayedPrice,
            maxDisplayedPrice, {
                it.getCurrencyFormatted()
            }, { min, max ->
                String.format(
                    getString(R.string.shop_discount_manage_discount_displayed_price_format),
                    min.getCurrencyFormatted(),
                    max.getCurrencyFormatted()
                )
            }
        )
        textDisplayedPrice.text = formattedDisplayedPrice
    }

    private fun setOriginalPrice(
        textOriginalPrice: Typography,
        uiModel: ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData,
        productStatus: ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus
    ) {
        val formattedOriginalPrice = getFormattedRangeString(
            uiModel.minOriginalPrice,
            uiModel.maxOriginalPrice, {
                it.getCurrencyFormatted()
            }, { min, max ->
                String.format(
                    getString(R.string.shop_discount_manage_discount_original_price_format),
                    min.getCurrencyFormatted(),
                    max.getCurrencyFormatted()
                )
            }
        )
        textOriginalPrice.shouldShowWithAction(productStatus.isProductDiscounted) {
            textOriginalPrice.apply {
                text = formattedOriginalPrice
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }

    private fun setLabelDiscount(
        labelDiscount: Label,
        uiModel: ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData,
        productStatus: ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus
    ) {
        val formattedDiscountPercentage = getFormattedRangeString(
            uiModel.minDiscountPercentage,
            uiModel.maxDiscountPercentage, {
                String.format(
                    getString(R.string.shop_discount_manage_discount_percent_format_non_range),
                    it
                )
            }, { min, max ->
                String.format(
                    getString(R.string.shop_discount_manage_discount_percent_format_range),
                    min.thousandFormatted(),
                    max.thousandFormatted()
                )
            }
        )
        labelDiscount.shouldShowWithAction(productStatus.isProductDiscounted) {
            labelDiscount.text = formattedDiscountPercentage
        }
    }

    private fun getFormattedRangeString(
        min: Int,
        max: Int,
        formatNonRange: (min: Int) -> String,
        formatWithRange: (min: Int, max: Int) -> String
    ): String {
        return if (min.isZero() && max.isZero()) {
            ""
        } else if (min == max) {
            formatNonRange(min)
        } else {
            formatWithRange(min, max)
        }
    }

}