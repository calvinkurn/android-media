package com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductWaitingForSelectionBinding
import com.tokopedia.tkpd.flashsale.domain.entity.enums.ProductStockStatus.*
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.WaitingForSelectionItem
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class WaitingForSelectionDelegateAdapter(
    private val onProductItemClicked: (Int) -> Unit,
    private val onCheckBoxClicked: (Int, Boolean) -> Unit):
DelegateAdapter<WaitingForSelectionItem, WaitingForSelectionDelegateAdapter.ViewHolder>(
WaitingForSelectionItem::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StfsItemProductWaitingForSelectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(
        item: WaitingForSelectionItem,
        viewHolder: WaitingForSelectionDelegateAdapter.ViewHolder
    ) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: StfsItemProductWaitingForSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { onProductItemClicked(adapterPosition) }
            binding.run{
                checkProductItem.apply {
                    setOnClickListener {
                        onCheckBoxClicked(checkProductItem.isChecked)
                    }
                }
            }
        }

        fun bind(item: WaitingForSelectionItem) {
            binding.run {
                tpgProductName.text = item.name
                imageProductItem.loadImage(item.picture)
                tpgDiscountedPrice.setDiscountedPrice(item)
                labelDiscount.setDiscount(item)
                tpgOriginalPrice.setPrice(item)
                tpgVariantStockLocation.setStock(item)
                checkProductItem.isVisible = item.isCheckBoxShown
                checkProductItem.isChecked = item.isSelected
            }
        }

        private fun onCheckBoxClicked(value: Boolean) {
            onCheckBoxClicked(adapterPosition, value)
        }

        private fun Typography.setDiscountedPrice(item: WaitingForSelectionItem) {
            text = if (item.discountedPrice.lowerPrice == item.discountedPrice.upperPrice) {
                item.discountedPrice.upperPrice.getCurrencyFormatted()
            } else {
                "${item.discountedPrice.lowerPrice.getCurrencyFormatted()} - ${item.discountedPrice.upperPrice.getCurrencyFormatted()}"

            }
        }

        private fun Label.setDiscount(item: WaitingForSelectionItem) {
            text = if (item.discount.lowerDiscount == item.discount.upperDiscount) {
                "${item.discount.upperDiscount}%"
            } else {
                "${item.discount.lowerDiscount}% - ${item.discount.upperDiscount}%"
            }
        }

        private fun Typography.setPrice(item: WaitingForSelectionItem) {
            strikethrough()
            text = if (item.price.lowerPrice == item.price.upperPrice) {
                item.price.upperPrice.getCurrencyFormatted()
            } else {
                "${item.price.lowerPrice.getCurrencyFormatted()} - ${item.price.upperPrice.getCurrencyFormatted()}"
            }
        }

        private fun Typography.setStock(item: WaitingForSelectionItem) {
            text = when (item.submittedProductStockStatus) {
                SINGLE_VARIANT_SINGLE_LOCATION -> MethodChecker.fromHtml(
                    context.getString(
                        R.string.stfs_variant_stock_non_variant_non_location_placeholder,
                        item.campaignStock
                    )
                )
                SINGLE_VARIANT_MULTI_LOCATION -> MethodChecker.fromHtml(
                    context.getString(
                        R.string.stfs_variant_stock_single_multiloc_placeholder,
                        item.campaignStock,
                        item.countLocation
                    )
                )
                MULTI_VARIANT_SINGLE_LOCATION -> MethodChecker.fromHtml(
                    context.getString(
                        R.string.stfs_variant_stock_variant_singleloc_placeholder,
                        item.totalChild,
                        item.campaignStock
                    )
                )
                MULTI_VARIANT_MULTI_LOCATION -> MethodChecker.fromHtml(
                    context.getString(
                        R.string.stfs_variant_stock_variant_multiloc_placeholder,
                        item.totalChild,
                        item.campaignStock,
                        item.countLocation
                    )
                )
            }
        }
    }
}
