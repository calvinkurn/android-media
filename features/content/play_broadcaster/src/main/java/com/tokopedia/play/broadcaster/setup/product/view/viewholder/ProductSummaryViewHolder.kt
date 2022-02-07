package com.tokopedia.play.broadcaster.setup.product.view.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemProductSummaryBodyListBinding
import com.tokopedia.play.broadcaster.databinding.ItemProductSummaryHeaderListBinding
import com.tokopedia.play.broadcaster.setup.product.view.adapter.ProductSummaryAdapter
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play_common.view.loadImage
import com.tokopedia.unifycomponents.Label

/**
 * Created By : Jonathan Darwin on February 07, 2022
 */
internal class ProductSummaryViewHolder private constructor() {

    internal class Header(
        private val binding: ItemProductSummaryHeaderListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductSummaryAdapter.Model.Header) {
            binding.tvProductSummaryTitle.text = item.text

            when(item.status) {
                CampaignStatus.Ongoing -> {
                    binding.tvProductSummaryLabelStatus.setLabel(
                        itemView.context.getString(R.string.play_bro_ongoing_campaign)
                    )
                    binding.tvProductSummaryLabelStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
                    binding.tvProductSummaryLabelStatus.visibility = View.VISIBLE
                }
                CampaignStatus.Ready, CampaignStatus.ReadyLocked -> {
                    binding.tvProductSummaryLabelStatus.setLabel(
                        itemView.context.getString(R.string.play_bro_upcoming_campaign)
                    )
                    binding.tvProductSummaryLabelStatus.setLabelType(Label.HIGHLIGHT_LIGHT_ORANGE)
                    binding.tvProductSummaryLabelStatus.visibility = View.VISIBLE
                }
                else -> binding.tvProductSummaryLabelStatus.visibility = View.GONE
            }
        }

        companion object {
            fun create(parent: ViewGroup): Header {
                return Header(
                    ItemProductSummaryHeaderListBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                )
            }
        }
    }

    internal class Body(
        private val binding: ItemProductSummaryBodyListBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductSummaryAdapter.Model.Body) {
            binding.ivProductSummaryImage.loadImage(item.product.imageUrl)
            binding.tvProductSummaryStock.text = itemView.context.getString(
                R.string.play_bro_product_chooser_stock,
                item.product.stock
            )
            binding.tvProductSummaryName.text = item.product.name

            when(item.product.price) {
                is OriginalPrice -> {
                    binding.tvProductSummaryPrice.text = item.product.price.price
                    binding.tvProductSummaryDiscount.visibility = View.GONE
                }
                is DiscountedPrice -> {
                    binding.tvProductSummaryPrice.text = item.product.price.discountedPrice
                    binding.tvProductSummaryOriginalPrice.text = item.product.price.originalPrice
                    binding.tvProductSummaryDiscount.text = itemView.context.getString(
                        R.string.play_bro_product_discount_template,
                        item.product.price.discountPercent
                    )
                    binding.tvProductSummaryDiscount.visibility = View.VISIBLE
                }
                else -> {
                    binding.tvProductSummaryPrice.text = ""
                    binding.tvProductSummaryDiscount.visibility = View.GONE
                }
            }

            binding.icProductSummaryDelete.setOnClickListener {
                listener.onProductDeleteClicked(item.product)
            }
        }

        companion object {
            fun create(parent: ViewGroup, listener: Listener): Body {
                return Body(
                    ItemProductSummaryBodyListBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    listener,
                )
            }
        }

        interface Listener {
            fun onProductDeleteClicked(product: ProductUiModel)
        }
    }
}