package com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductOnFinishedSelectionBinding
import com.tokopedia.tkpd.flashsale.domain.entity.enums.ProductStockStatus
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.FinishedProcessSelectionItem
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class FinishedProcessSelectionDelegateAdapter(
    private val onProductItemClicked: (Int) -> Unit
) :
    DelegateAdapter<FinishedProcessSelectionItem, FinishedProcessSelectionDelegateAdapter.ViewHolder>(
        FinishedProcessSelectionItem::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StfsItemProductOnFinishedSelectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(
        item: FinishedProcessSelectionItem,
        viewHolder: FinishedProcessSelectionDelegateAdapter.ViewHolder
    ) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: StfsItemProductOnFinishedSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { onProductItemClicked(adapterPosition) }
        }

        fun bind(item: FinishedProcessSelectionItem) {
            binding.run {
                tpgProductName.text = item.name
                imageProductItem.loadImage(item.picture)
                tpgDiscountedPrice.setDiscountedPrice(item)
                labelDiscount.setDiscount(item)
                tpgOriginalPrice.setPrice(item)
                tpgSubsidy.setSubsidy(item)
                tpgVariantStockLocation.setStock(item)
            }
        }

        private fun Typography.setDiscountedPrice(item: FinishedProcessSelectionItem) {
            text = if (item.discountedPrice.lowerPrice == item.discountedPrice.upperPrice) {
                item.discountedPrice.upperPrice.getCurrencyFormatted()
            } else {
                "${item.discountedPrice.lowerPrice.getCurrencyFormatted()} - ${item.discountedPrice.upperPrice.getCurrencyFormatted()}"

            }
        }

        private fun Label.setDiscount(item: FinishedProcessSelectionItem) {
            text = if (item.discount.lowerDiscount == item.discount.upperDiscount) {
                "${item.discount.upperDiscount}%"
            } else {
                "${item.discount.lowerDiscount}% - ${item.discount.upperDiscount}%"
            }
        }

        private fun Typography.setPrice(item: FinishedProcessSelectionItem) {
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            text = if (item.price.lowerPrice == item.price.upperPrice) {
                item.price.upperPrice.getCurrencyFormatted()
            } else {
                "${item.price.lowerPrice.getCurrencyFormatted()} - ${item.price.upperPrice.getCurrencyFormatted()}"
            }
        }

        private fun Typography.setStock(item: FinishedProcessSelectionItem) {
            text = when (item.submittedProductStockStatus) {
                ProductStockStatus.SINGLE_VARIANT_SINGLE_LOCATION -> MethodChecker.fromHtml(
                    context.getString(
                        R.string.stfs_variant_stock_non_variant_non_location_placeholder,
                        item.campaignStock
                    )
                )
                ProductStockStatus.SINGLE_VARIANT_MULTI_LOCATION -> MethodChecker.fromHtml(
                    context.getString(
                        R.string.stfs_variant_stock_single_multiloc_placeholder,
                        item.campaignStock,
                        item.warehouses.count()
                    )
                )
                ProductStockStatus.MULTI_VARIANT_SINGLE_LOCATION -> MethodChecker.fromHtml(
                    context.getString(
                        R.string.stfs_variant_stock_variant_singleloc_placeholder,
                        item.totalChild,
                        item.campaignStock
                    )
                )
                ProductStockStatus.MULTI_VARIANT_MULTI_LOCATION -> MethodChecker.fromHtml(
                    context.getString(
                        R.string.stfs_variant_stock_variant_multiloc_placeholder,
                        item.totalChild,
                        item.campaignStock,
                        item.warehouses.count()
                    )
                )
            }
        }

        private fun Typography.setSubsidy(item: FinishedProcessSelectionItem) {
            var subsidyAmount = 0L
            if (item.isMultiwarehouse) {
                item.warehouses.forEach { warehouse ->
                    subsidyAmount += warehouse.subsidy.subsidyAmount
                }
            }
            if (subsidyAmount.isMoreThanZero()) {
                this.apply {
                    visible()
                    text = MethodChecker.fromHtml(
                        itemView.context.getString(
                            R.string.stfs_subsidy_value_placeholder,
                            subsidyAmount.getCurrencyFormatted()
                        )
                    )
                }
            } else {
                this.gone()
            }
        }
    }
}