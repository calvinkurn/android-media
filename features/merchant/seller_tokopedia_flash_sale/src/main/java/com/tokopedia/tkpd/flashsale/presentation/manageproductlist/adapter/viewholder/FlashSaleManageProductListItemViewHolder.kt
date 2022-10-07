package com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.viewholder

import android.graphics.Paint
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductListItemBinding
import com.tokopedia.campaign.utils.formatter.NumberRangeFormatterUtil
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.item.FlashSaleManageProductListItem
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography


class FlashSaleManageProductListItemViewHolder(
    private val binding: LayoutCampaignManageProductListItemBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    interface Listener {
        fun onManageProductButtonClicked(productData: ReservedProduct.Product)
        fun onDeleteProductButtonClicked(productData: ReservedProduct.Product)
    }

    private val productImageView: ImageUnify by lazy {
        binding.imgProduct
    }
    private val textProductName: Typography by lazy {
        binding.textProductName
    }
    private val labelTotalVariant: Label by lazy {
        binding.labelTotalVariant
    }
    private val labelTotalDiscountedVariant: Label by lazy {
        binding.labelTotalDiscountedVariant
    }
    private val textDisplayedPrice: Typography by lazy {
        binding.textDisplayedPrice
    }
    private val labelDiscountPercentage: Label by lazy {
        binding.labelDiscount
    }
    private val textOriginalPrice: Typography by lazy {
        binding.textOriginalPrice
    }
    private val divider: DividerUnify by lazy {
        binding.divider
    }
    private val textTotalStock: Typography by lazy {
        binding.textTotalStock
    }
    val buttonManage: UnifyButton by lazy {
        binding.buttonManageDiscount
    }
    private val imageDeleteProduct: ImageUnify by lazy {
        binding.imageDeleteProduct
    }

    fun bind(productItem: FlashSaleManageProductListItem) {
        productItem.product?.let { productData ->
            configProductImage(productData)
            configProductName(productData)
            configProductLabelSection(productData)
            configDisplayedPrice(productData)
            configDiscountLabel(productData)
            configOriginalPrice(productData)
            configTotalStockData(productData)
            configDivider()
            configManageDiscountButton(productData)
            configDeleteProductButton(productData)
        }
    }

    private fun configDeleteProductButton(productData: ReservedProduct.Product) {
        imageDeleteProduct.setOnClickListener {
            listener.onDeleteProductButtonClicked(productData)
        }
    }

    private fun configManageDiscountButton(productData: ReservedProduct.Product) {
        buttonManage.apply {
            if (productData.isDiscounted()) {
                buttonType = UnifyButton.Type.ALTERNATE
                text = getString(R.string.stfs_product_item_edit_button_text)
            } else {
                buttonType = UnifyButton.Type.MAIN
                text = getString(R.string.stfs_product_item_manage_button_text)
            }
            setOnClickListener {
                listener.onManageProductButtonClicked(productData)
            }
        }
    }

    private fun configDivider() {
        divider.showWithCondition(textTotalStock.isShown)
    }

    private fun configTotalStockData(productData: ReservedProduct.Product) {
        val totalCampaignStock = productData.getDiscountedProductCampaignStock()
        val totalLocation = productData.getTotalDiscountedLocation()
        textTotalStock.shouldShowWithAction(
            !totalCampaignStock.isZero() && productData.isDiscounted()
        ) {
            val totalStockFormattedString =
                when (totalLocation) {
                    Int.ZERO, Int.ONE -> {
                        getString(
                            R.string.stfs_product_item_total_stock_non_multi_loc_format,
                            totalCampaignStock
                        )
                    }
                    else -> {
                        getString(
                            R.string.stfs_product_item_total_stock_multi_loc_format,
                            totalCampaignStock
                        )
                    }
                }
            textTotalStock.text = MethodChecker.fromHtml(totalStockFormattedString)
        }
    }

    private fun configOriginalPrice(productData: ReservedProduct.Product) {
        val formattedOriginalPrice = NumberRangeFormatterUtil.getFormattedRangeString(
            productData.price.lowerPrice,
            productData.price.upperPrice, {
                it.getCurrencyFormatted()
            }, { min, max ->
                getString(
                    R.string.stfs_product_item_original_price_format,
                    min.getCurrencyFormatted(),
                    max.getCurrencyFormatted()
                )

            }
        )
        textOriginalPrice.shouldShowWithAction(productData.isDiscounted()) {
            textOriginalPrice.apply {
                text = formattedOriginalPrice
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }

    private fun configDiscountLabel(productData: ReservedProduct.Product) {
        val formattedDiscountPercentage = NumberRangeFormatterUtil.getFormattedRangeString(
            productData.getMinDiscountPercentage().toLong(),
            productData.getMaxDiscountPercentage().toLong(), {
                getString(
                    R.string.stfs_product_item_discount_label_percentage_format_non_range,
                    it
                )
            }, { min, max ->
                getString(
                    R.string.stfs_product_item_discount_label_percentage_format_range,
                    min.thousandFormatted(),
                    max.thousandFormatted()
                )
            }
        )
        labelDiscountPercentage.shouldShowWithAction(productData.isDiscounted()) {
            labelDiscountPercentage.text = formattedDiscountPercentage
        }
    }

    private fun configProductImage(productData: ReservedProduct.Product) {
        productImageView.loadImage(productData.picture)
    }

    private fun configProductName(productData: ReservedProduct.Product) {
        textProductName.text = productData.name
    }

    private fun configDisplayedPrice(productData: ReservedProduct.Product) {
        val minDisplayedPrice: Long
        val maxDisplayedPrice: Long
        if (productData.isDiscounted()) {
            minDisplayedPrice = productData.getMinDiscountedPrice().orZero()
            maxDisplayedPrice = productData.getMaxDiscountedPrice().orZero()
        } else {
            minDisplayedPrice = productData.price.lowerPrice.orZero()
            maxDisplayedPrice = productData.price.upperPrice.orZero()
        }
        val formattedDisplayedPrice = NumberRangeFormatterUtil.getFormattedRangeString(
            minDisplayedPrice,
            maxDisplayedPrice, {
                it.getCurrencyFormatted()
            }, { min, max ->
                getString(
                    R.string.stfs_product_item_displayed_price_format,
                    min.getCurrencyFormatted(),
                    max.getCurrencyFormatted()
                )

            }
        )
        textDisplayedPrice.text = formattedDisplayedPrice
    }

    private fun configProductLabelSection(productData: ReservedProduct.Product?) {
        val totalChild = productData?.childProducts?.size.orZero()
        labelTotalVariant.shouldShowWithAction(totalChild.isMoreThanZero()) {
            labelTotalVariant.setLabel(
                getString(R.string.stfs_product_item_total_variant, totalChild.toString())
            )
        }
        val totalDiscountedChild = productData?.childProducts?.count {
            it.warehouses.any { warehouse ->
                !warehouse.discountSetup.discount.isZero()
            }
        }
        labelTotalDiscountedVariant.shouldShowWithAction(totalDiscountedChild.isMoreThanZero()) {
            labelTotalDiscountedVariant.setLabel(
                getString(
                    R.string.stfs_product_item_total_discounted_variant,
                    totalDiscountedChild.toString()
                )
            )
        }
    }

    private fun getString(@StringRes stringRes: Int, vararg value: Any): String {
        return itemView.context.getString(stringRes, *value)
    }
}
