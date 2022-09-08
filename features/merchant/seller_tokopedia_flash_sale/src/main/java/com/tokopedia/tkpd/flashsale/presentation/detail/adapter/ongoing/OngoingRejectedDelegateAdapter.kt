package com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductOngoingRejectedBinding
import com.tokopedia.tkpd.flashsale.domain.entity.enums.ProductStockStatus
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.item.OngoingRejectedItem
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class OngoingRejectedDelegateAdapter(
    private val onProductItemClicked: (Int) -> Unit
) : DelegateAdapter<OngoingRejectedItem, OngoingRejectedDelegateAdapter.ViewHolder>(
    OngoingRejectedItem::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StfsItemProductOngoingRejectedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(
        item: OngoingRejectedItem,
        viewHolder: OngoingRejectedDelegateAdapter.ViewHolder
    ) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: StfsItemProductOngoingRejectedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { onProductItemClicked(adapterPosition) }
        }

        fun bind(item: OngoingRejectedItem) {
            binding.run {
                tpgProductName.text = item.name
                imageProductItem.loadImage(item.picture)
                tpgDiscountedPrice.setDiscountedPrice(item)
                labelDiscount.setDiscount(item)
                tpgOriginalPrice.setPrice(item)
                tpgProductSold.setSoldCount()
                tpgVariantStockLocation.setStock(item)
                tpgRejectionReason.setRejectReason(item)
            }
        }

        private fun Typography.setDiscountedPrice(item: OngoingRejectedItem) {
            text = if (item.discountedPrice.lowerPrice == item.discountedPrice.upperPrice) {
                item.discountedPrice.upperPrice.getCurrencyFormatted()
            } else {
                "${item.discountedPrice.lowerPrice.getCurrencyFormatted()} - ${item.discountedPrice.upperPrice.getCurrencyFormatted()}"

            }
        }

        private fun Label.setDiscount(item: OngoingRejectedItem) {
            text = if (item.discount.lowerDiscount == item.discount.upperDiscount) {
                "${item.discount.upperDiscount}%"
            } else {
                "${item.discount.lowerDiscount}% - ${item.discount.upperDiscount}%"
            }
        }

        private fun Typography.setPrice(item: OngoingRejectedItem) {
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            text = if (item.price.lowerPrice == item.price.upperPrice) {
                item.price.upperPrice.getCurrencyFormatted()
            } else {
                "${item.price.lowerPrice.getCurrencyFormatted()} - ${item.price.upperPrice.getCurrencyFormatted()}"
            }
        }

        private fun Typography.setStock(item: OngoingRejectedItem) {
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

        private fun Typography.setSoldCount() {
            text = MethodChecker.fromHtml(
                context.getString(R.string.stfs_product_sold_rejected_value_placeholder)
            )
        }

        private fun Typography.setRejectReason(item: OngoingRejectedItem) {
            val isWarehouseAvailable = item.warehouses.size.isMoreThanZero()
            val isRejectReasonAvailable = item.warehouses[Int.ZERO].rejectionReason.isNotEmpty()
            this.run {
                if (isWarehouseAvailable && isRejectReasonAvailable) {
                    visible()
                    text = MethodChecker.fromHtml(
                        context.getString(
                            R.string.stfs_rejection_reason_placeholder,
                            item.warehouses[Int.ZERO].rejectionReason
                        )
                    )
                } else {
                    gone()
                }
            }
        }
    }
}