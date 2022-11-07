package com.tokopedia.play.broadcaster.setup.product.view.viewholder

import android.content.Context
import android.graphics.Paint
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifyprinciples.R as unifyR
import com.tokopedia.play.broadcaster.databinding.ItemProductSummaryBodyListBinding
import com.tokopedia.play.broadcaster.databinding.ItemProductSummaryHeaderListBinding
import com.tokopedia.play.broadcaster.setup.product.view.adapter.ProductSummaryAdapter
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play_common.util.extension.buildSpannedString
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

        init {
            binding.tvProductSummaryOriginalPrice.paintFlags =
                binding.tvProductSummaryOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        private val ctx: Context
            get() = itemView.context

        private val baseColor: ForegroundColorSpan
            get() = ForegroundColorSpan(MethodChecker.getColor(ctx, unifyR.color.Unify_NN950))

        private val fgColor: ForegroundColorSpan
            get() = ForegroundColorSpan(MethodChecker.getColor(ctx, unifyR.color.Unify_G500))

        fun bind(item: ProductSummaryAdapter.Model.Body) {
            binding.ivProductSummaryImage.loadImage(item.product.imageUrl)
            binding.tvProductSummaryName.text = item.product.name

            binding.tvPinnedProductCarouselInfo.apply {
                text = buildSpannedString {
                    if(item.product.pinStatus.isPinned) {
                        append(ctx.getString(R.string.play_bro_pinned_product_info), fgColor, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                        if (item.product.stock > 0) append(" • ", baseColor, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                    }
                    if(item.product.stock > 0) append(ctx.getString(R.string.play_bro_product_chooser_stock, item.product.stock), baseColor, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                }
                visibility = if(text.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            binding.ivProductSummaryCover.showWithCondition(item.product.stock <= 0)
            binding.tvProductSummaryEmptyStock.showWithCondition(item.product.stock <= 0)

            when(item.product.price) {
                is OriginalPrice -> {
                    binding.tvProductSummaryPrice.text = item.product.price.price
                    binding.labelProductSummaryDiscount.visibility = View.GONE
                    binding.tvProductSummaryOriginalPrice.visibility = View.GONE
                }
                is DiscountedPrice -> {
                    binding.tvProductSummaryPrice.text = item.product.price.discountedPrice
                    binding.tvProductSummaryOriginalPrice.text = item.product.price.originalPrice
                    binding.labelProductSummaryDiscount.text = itemView.context.getString(
                        R.string.play_bro_product_discount_template,
                        item.product.price.discountPercent
                    )
                    binding.labelProductSummaryDiscount.visibility = View.VISIBLE
                    binding.tvProductSummaryOriginalPrice.visibility = View.VISIBLE
                }
                else -> {
                    binding.tvProductSummaryPrice.text = ""
                    binding.labelProductSummaryDiscount.visibility = View.GONE
                    binding.tvProductSummaryOriginalPrice.visibility = View.GONE
                }
            }

            binding.icProductSummaryDelete.setOnClickListener {
                listener.onProductDeleteClicked(item.product)
            }
            binding.ivPinnedProductCarouselInfo.showWithCondition(item.product.pinStatus.isPinned)
            if (item.product.pinStatus.isPinned) listener.onImpressPinnedProduct(item.product)

            binding.viewPinProduct.showWithCondition(item.product.pinStatus.canPin && item.isEligibleForPin)
            binding.viewPinProduct.setupPinned(item.product.pinStatus.isPinned, item.product.pinStatus.isLoading)
            binding.viewPinProduct.setOnClickListener {
                listener.onPinClicked(item.product)
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
            fun onPinClicked(product: ProductUiModel)
            fun onImpressPinnedProduct(product: ProductUiModel)
        }
    }
}