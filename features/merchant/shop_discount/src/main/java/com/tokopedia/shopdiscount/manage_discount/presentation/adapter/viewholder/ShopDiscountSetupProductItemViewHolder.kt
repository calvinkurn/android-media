package com.tokopedia.shopdiscount.manage_discount.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.ShopDiscountSetupProductItemLayoutBinding
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.ALL_ABUSIVE_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.NO_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.PARTIAL_ABUSIVE_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.R2_ABUSIVE_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.VALUE_ERROR
import com.tokopedia.shopdiscount.utils.formatter.RangeFormatterUtil
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
            setLabelTotalProductVariant(
                this,
                uiModel.mappedResultData.totalVariant,
                uiModel.mappedResultData.totalDiscountedVariant,
                uiModel.productStatus.isProductDiscounted,
                uiModel.productStatus.errorType
            )
            setDisplayedPrice(textDisplayedPrice, uiModel.mappedResultData, uiModel.productStatus)
            setLabelDiscount(labelDiscount, uiModel.mappedResultData, uiModel.productStatus)
            setOriginalPrice(textOriginalPrice, uiModel.mappedResultData, uiModel.productStatus)
            setTotalStockData(textTotalStock, uiModel)
            setErrorState(textErrorValidation, imageWarning, uiModel)
            showDivider(this)
            setManageDiscountButton(buttonManageDiscount, uiModel)
            setDeleteProductButton(imageDeleteProduct, uiModel)
        }
    }

    private fun setErrorState(
        textErrorValidation: Typography,
        imageWarning: ImageUnify,
        uiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ) {
        val errorType = uiModel.productStatus.errorType
        textErrorValidation.shouldShowWithAction(errorType != NO_ERROR) {
            textErrorValidation.text = when (errorType) {
                ALL_ABUSIVE_ERROR -> {
                    getString(R.string.shop_discount_manage_discount_abusive_all)
                }
                PARTIAL_ABUSIVE_ERROR -> {
                    getString(R.string.shop_discount_manage_discount_abusive_partial)
                }
                else -> {
                    getString(R.string.shop_discount_manage_discount_value_error)
                }
            }
        }
        imageWarning.shouldShowWithAction(shouldShowImageWarning(errorType)) {}
    }

    private fun shouldShowImageWarning(errorType: Int): Boolean {
        return errorType == ALL_ABUSIVE_ERROR || errorType == PARTIAL_ABUSIVE_ERROR
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
        val totalStock = getTotalStock(uiModel)
        val totalLocation = uiModel.mappedResultData.totalLocation
        textTotalStock.shouldShowWithAction(
            !totalStock.isZero() && uiModel.productStatus.errorType == NO_ERROR && uiModel.productStatus.isProductDiscounted
        ) {
            val totalStockFormattedString =
                when {
                    shouldUseNonVariantMultiLocTotalStockFormat(uiModel.productStatus) -> {
                        String.format(
                            getString(R.string.sd_total_stock_multiple_location),
                            totalStock,
                            totalLocation
                        )
                    }
                    shouldUseVariantMultiLocTotalStockFormat(uiModel.productStatus) -> {
                        String.format(
                            getString(R.string.sd_total_stock_various_multiple_location),
                            totalStock
                        )
                    }
                    else -> {
                        String.format(
                            getString(R.string.sd_total_stock),
                            totalStock
                        )
                    }
                }
            textTotalStock.text = MethodChecker.fromHtml(totalStockFormattedString)
        }
    }

    private fun getTotalStock(uiModel: ShopDiscountSetupProductUiModel.SetupProductData): Int {
        return if (uiModel.productStatus.isVariant) {
            uiModel.listProductVariant.sumOf {
                it.stock.toIntOrZero()
            }
        } else {
            uiModel.stock.toIntOrZero()
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
            if (uiModel.productStatus.isProductDiscounted && !isError(uiModel.productStatus.errorType)) {
                isEnabled = true
                buttonType = UnifyButton.Type.ALTERNATE
                text = getString(R.string.shop_discount_manage_discount_edit_toolbar_title)
            } else {
                isEnabled = when (uiModel.productStatus.errorType) {
                    ALL_ABUSIVE_ERROR -> {
                        false
                    }
                    PARTIAL_ABUSIVE_ERROR -> {
                        true
                    }
                    VALUE_ERROR -> {
                        true
                    }
                    R2_ABUSIVE_ERROR -> {
                        true
                    }
                    else -> {
                        true
                    }
                }
                buttonType = UnifyButton.Type.MAIN
                text = getString(R.string.shop_discount_manage_discount_manage_toolbar_title)
            }
            setOnClickListener {
                listener.onClickManageDiscount(uiModel)
            }
        }
    }

    private fun isError(errorType: Int): Boolean {
        return errorType != NO_ERROR
    }

    private fun setLabelTotalProductVariant(
        binding: ShopDiscountSetupProductItemLayoutBinding,
        totalVariant: Int,
        totalDiscountedVariant: Int,
        isProductDiscounted: Boolean,
        errorType: Int
    ) {
        binding.labelTotalVariant.shouldShowWithAction(!totalVariant.isZero()) {
            binding.labelTotalVariant.apply {
                text = String.format(
                    getString(R.string.shop_discount_manage_discount_total_variant_format),
                    totalVariant.toString()
                )
            }
        }
        binding.labelTotalDiscountedVariant.shouldShowWithAction(
            !totalDiscountedVariant.isZero() &&
                isProductDiscounted &&
                !isError(errorType)
        ) {
            binding.labelTotalDiscountedVariant.apply {
                text = String.format(
                    getString(R.string.shop_discount_manage_discount_total_discounted_variant_format),
                    totalDiscountedVariant.toString()
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
        if (productStatus.isProductDiscounted && !isError(productStatus.errorType)) {
            minDisplayedPrice = uiModel.minDisplayedPrice
            maxDisplayedPrice = uiModel.maxDisplayedPrice
        } else {
            minDisplayedPrice = uiModel.minOriginalPrice
            maxDisplayedPrice = uiModel.maxOriginalPrice
        }
        val formattedDisplayedPrice = RangeFormatterUtil.getFormattedRangeString(
            minDisplayedPrice,
            maxDisplayedPrice,
            {
                it.getCurrencyFormatted()
            },
            { min, max ->
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
        val formattedOriginalPrice = RangeFormatterUtil.getFormattedRangeString(
            uiModel.minOriginalPrice,
            uiModel.maxOriginalPrice,
            {
                it.getCurrencyFormatted()
            },
            { min, max ->
                String.format(
                    getString(R.string.shop_discount_manage_discount_original_price_format),
                    min.getCurrencyFormatted(),
                    max.getCurrencyFormatted()
                )
            }
        )
        textOriginalPrice.shouldShowWithAction(productStatus.isProductDiscounted && !isError(productStatus.errorType)) {
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
        val formattedDiscountPercentage = RangeFormatterUtil.getFormattedRangeString(
            uiModel.minDiscountPercentage,
            uiModel.maxDiscountPercentage,
            {
                String.format(
                    getString(R.string.shop_discount_manage_discount_percent_format_non_range),
                    it
                )
            },
            { min, max ->
                String.format(
                    getString(R.string.shop_discount_manage_discount_percent_format_range),
                    min.thousandFormatted(),
                    max.thousandFormatted()
                )
            }
        )
        labelDiscount.shouldShowWithAction(productStatus.isProductDiscounted && !isError(productStatus.errorType)) {
            labelDiscount.text = formattedDiscountPercentage
        }
    }
}
