package com.tokopedia.content.product.picker.seller.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.product.picker.R
import com.tokopedia.content.product.picker.databinding.ItemProductSummaryBodyListBinding
import com.tokopedia.content.product.picker.databinding.ItemProductSummaryHeaderListBinding
import com.tokopedia.content.product.picker.seller.model.DiscountedPrice
import com.tokopedia.content.product.picker.seller.model.OriginalPrice
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignStatus
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.view.adapter.ProductSummaryAdapter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isLessThanEqualZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play_common.view.loadImage
import com.tokopedia.unifycomponents.Label
import com.tokopedia.content.product.picker.R as contentproductpickerR
import com.tokopedia.play_common.R as play_commonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created By : Jonathan Darwin on February 07, 2022
 */
class ProductSummaryViewHolder private constructor() {

    class Header(
        private val binding: ItemProductSummaryHeaderListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductSummaryAdapter.Model.Header) {
            binding.tvProductSummaryTitle.text = item.text

            when (item.status) {
                CampaignStatus.Ongoing -> {
                    binding.tvProductSummaryLabelStatus.setLabel(
                        itemView.context.getString(R.string.ongoing_campaign)
                    )
                    binding.tvProductSummaryLabelStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
                    binding.tvProductSummaryLabelStatus.visible()
                }
                CampaignStatus.Ready, CampaignStatus.ReadyLocked -> {
                    binding.tvProductSummaryLabelStatus.setLabel(
                        itemView.context.getString(R.string.upcoming_campaign)
                    )
                    binding.tvProductSummaryLabelStatus.setLabelType(Label.HIGHLIGHT_LIGHT_ORANGE)
                    binding.tvProductSummaryLabelStatus.visible()
                }
                else -> binding.tvProductSummaryLabelStatus.gone()
            }
        }

        companion object {
            fun create(parent: ViewGroup): Header {
                return Header(
                    ItemProductSummaryHeaderListBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    class Body(
        private val binding: ItemProductSummaryBodyListBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvProductSummaryOriginalPrice.paintFlags =
                binding.tvProductSummaryOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        private val ctx: Context
            get() = itemView.context

        private val fgColor: ForegroundColorSpan
            get() = ForegroundColorSpan(MethodChecker.getColor(ctx, unifyprinciplesR.color.Unify_GN500))

        @SuppressLint("ResourceType")
        fun bind(item: ProductSummaryAdapter.Model.Body) {
            binding.ivProductSummaryImage.loadImage(item.product.imageUrl)
            binding.tvProductSummaryStock.text = String.format(ctx.getString(R.string.product_stock), item.product.stock)
            binding.tvProductSummaryName.text = item.product.name

            binding.tvCommissionExtra.background = ContextCompat.getDrawable(
                ctx,
                contentproductpickerR.drawable.bg_commission_extra
            )
            binding.tvCommission.text = ctx.getString(R.string.product_affiliate_commission_fmt, item.product.commissionFmt)
            binding.tvCommission.showWithCondition(item.product.hasCommission)
            binding.tvCommissionExtra.showWithCondition(item.product.hasCommission && item.product.extraCommission)

            binding.tvProductSummaryEmptyStock.background = ContextCompat.getDrawable(
                ctx,
                play_commonR.drawable.bg_play_product_tag_stock
            )
            binding.tvProductSummaryEmptyStock.showWithCondition(item.product.stock.isLessThanEqualZero())
            binding.tvProductSummaryEmptyStock.showWithCondition(item.product.stock.isLessThanEqualZero())
            binding.tvProductSummaryEmptyStock.text = ctx.getString(
                if (item.product.pinStatus.isPinned) {
                    R.string.product_tag_stock_empty_pinned
                } else {
                    R.string.product_tag_stock_empty
                }
            )

            when (val productPrice = item.product.price) {
                is OriginalPrice -> {
                    binding.tvProductSummaryPrice.text = productPrice.price
                    binding.labelProductSummaryDiscount.gone()
                    binding.tvProductSummaryOriginalPrice.gone()
                }
                is DiscountedPrice -> {
                    binding.tvProductSummaryPrice.text = productPrice.discountedPrice
                    binding.tvProductSummaryOriginalPrice.text = productPrice.originalPrice
                    binding.labelProductSummaryDiscount.text = itemView.context.getString(
                        R.string.product_discount_template,
                        productPrice.discountPercent
                    )
                    binding.labelProductSummaryDiscount.visible()
                    binding.tvProductSummaryOriginalPrice.visible()
                }
                else -> {
                    binding.tvProductSummaryPrice.text = ""
                    binding.labelProductSummaryDiscount.gone()
                    binding.tvProductSummaryOriginalPrice.gone()
                }
            }

            binding.icProductSummaryDelete.setOnClickListener {
                listener.onProductDeleteClicked(item.product)
            }
            if (item.product.pinStatus.isPinned) listener.onImpressPinnedProduct(item.product)

            binding.viewPinProduct.showWithCondition(item.product.pinStatus.canPin && item.isEligibleForPin)
            binding.viewPinProduct.setupPinned(item.product.pinStatus.isPinned, item.product.pinStatus.isLoading)
            binding.viewPinProduct.setOnClickListener {
                listener.onPinClicked(item.product)
            }
            binding.tvSummaryProductTagNumber.background = ContextCompat.getDrawable(
                ctx,
                contentproductpickerR.drawable.product_number_background
            )
            binding.tvSummaryProductTagNumber.text = item.product.number
            binding.tvSummaryProductTagNumber.showWithCondition(item.isNumerationShown)

            binding.productTagRate.text = item.product.rating
            binding.productTagSold.text = item.product.countSold
            binding.productTagShopBadge.loadImage(item.product.shopBadge)
            binding.productTagShopName.text = item.product.shopName

            setProductUserInfo(item)
        }

        private fun setProductUserInfo(item: ProductSummaryAdapter.Model.Body) {
            if (!item.selectedAccount.isUser) {
                binding.groupUgcInfo.hide()
            } else {
                binding.groupUgcInfo.show()

                // hide star icon and rate text when rate empty
                binding.productTagRateIcon.showWithCondition(item.product.rating.isNotBlank())
                binding.productTagRate.showWithCondition(item.product.rating.isNotBlank())

                // hide dots when rate or soldCount is empty
                binding.productTagDots.showWithCondition(
                    item.product.rating.isNotBlank() &&
                        item.product.countSold.isNotBlank()
                )

                // hide sold text when countSold empty
                binding.productTagSold.showWithCondition(item.product.countSold.isNotBlank())

                // hide shop name when shop name empty
                binding.productTagShopName.showWithCondition(item.product.shopName.isNotBlank())

                // hide shop badge icon when shop badge empty or shop name empty
                binding.productTagShopBadge.showWithCondition(
                    item.product.shopName.isNotBlank() &&
                        item.product.shopBadge.isNotBlank()
                )
            }
        }

        companion object {
            fun create(parent: ViewGroup, listener: Listener): Body {
                return Body(
                    ItemProductSummaryBodyListBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    listener
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
