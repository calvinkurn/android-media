package com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductOngoingBinding
import com.tokopedia.tkpd.flashsale.domain.entity.enums.ProductStockStatus
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.item.OngoingItem
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class OngoingDelegateAdapter(
    private val onProductItemClicked: (Int) -> Unit
) : DelegateAdapter<OngoingItem, OngoingDelegateAdapter.ViewHolder>(
    OngoingItem::class.java
) {

    companion object {
        private const val STATUS_ACCEPTED = "Produk Diterima"
        private const val STATUS_REJECTED = "Produk Ditolak"
    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StfsItemProductOngoingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(
        item: OngoingItem,
        viewHolder: OngoingDelegateAdapter.ViewHolder
    ) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: StfsItemProductOngoingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { onProductItemClicked(adapterPosition) }
        }

        fun bind(item: OngoingItem) {
            binding.run {
                tpgProductName.text = item.name
                imageProductItem.loadImage(item.picture)
                tpgDiscountedPrice.setDiscountedPrice(item)
                labelDiscount.setDiscount(item)
                tpgOriginalPrice.setPrice(item)
                tpgSubsidy.setSubsidy(item)
                tpgProductSold.setSoldCount(item)
                tpgVariantStockLocation.setStock(item)
            }
            setRejectReason(item)
            setStatusIcon(item)
        }

        private fun setRejectReason(item: OngoingItem) {
            binding.run {
                if (item.isMultiwarehouse && !item.isParentProduct) {
                    tpgRejectionReason.gone()
                } else {
                    tpgRejectionReason.setRejectReason(item)
                }
            }
        }

        private fun setStatusIcon(item: OngoingItem) {
            binding.run {
                when(item.statusText) {
                    STATUS_ACCEPTED -> {
                        iconInfo.visible()
                        iconWarning.invisible()
                    }
                    STATUS_REJECTED -> {
                        iconInfo.invisible()
                        iconWarning.visible()
                    }
                }
            }
        }

        private fun Typography.setDiscountedPrice(item: OngoingItem) {
            text = if (item.discountedPrice.lowerPrice == item.discountedPrice.upperPrice) {
                item.discountedPrice.upperPrice.getCurrencyFormatted()
            } else {
                "${item.discountedPrice.lowerPrice.getCurrencyFormatted()} - ${item.discountedPrice.upperPrice.getCurrencyFormatted()}"

            }
        }

        private fun Label.setDiscount(item: OngoingItem) {
            text = if (item.discount.lowerDiscount == item.discount.upperDiscount) {
                "${item.discount.upperDiscount}%"
            } else {
                "${item.discount.lowerDiscount}% - ${item.discount.upperDiscount}%"
            }
        }

        private fun Typography.setPrice(item: OngoingItem) {
            strikethrough()
            text = if (item.price.lowerPrice == item.price.upperPrice) {
                item.price.upperPrice.getCurrencyFormatted()
            } else {
                "${item.price.lowerPrice.getCurrencyFormatted()} - ${item.price.upperPrice.getCurrencyFormatted()}"
            }
        }

        private fun Typography.setSubsidy(item: OngoingItem) {
            val subsidyAmount = item.totalSubsidy
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

        private fun Typography.setStock(item: OngoingItem) {
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
                        item.countLocation
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
                        item.countLocation
                    )
                )
            }
        }

        private fun Typography.setSoldCount(item: OngoingItem) {
            text = MethodChecker.fromHtml(
                context.getString(
                    R.string.stfs_product_sold_value_placeholder,
                    item.soldCount
                )
            )
        }

        private fun Typography.setRejectReason(item: OngoingItem) {
            val isWarehouseAvailable = item.warehouses.size.isMoreThanZero()
            val isRejectReasonAvailable = if (isWarehouseAvailable) {
                item.warehouses.firstOrNull()?.rejectionReason?.isNotEmpty()
            } else {
                false
            }
            this.run {
                if (isWarehouseAvailable && isRejectReasonAvailable == true) {
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
