package com.tokopedia.play.ui.view.carousel.viewholder

import android.content.Context
import android.graphics.Paint
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ItemPlayPinnedProductBinding
import com.tokopedia.play.view.type.ComingSoon
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.OutOfStock
import com.tokopedia.play.view.type.StockAvailable
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play_common.util.extension.buildSpannedString
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by kenny.hadisaputra on 08/07/22
 */
class ProductCarouselViewHolder private constructor() {

    class PinnedProduct(
        private val binding: ItemPlayPinnedProductBinding,
        private val listener: Listener,
    ) : BaseViewHolder(binding.root) {

        private val context: Context
            get() = binding.root.context

        private val separatorSpan = ForegroundColorSpan(
            MethodChecker.getColor(context, unifyR.color.Unify_NN500)
        )
        private val stockSpan = ForegroundColorSpan(
            MethodChecker.getColor(context, unifyR.color.Unify_RN500)
        )

        private val iconCartEnabled = getIconUnifyDrawable(
            context,
            IconUnify.CART,
            MethodChecker.getColor(context, unifyR.color.Unify_GN500),
        )

        private val iconCartDisabled by lazy {
            getIconUnifyDrawable(
                context,
                IconUnify.CART,
                MethodChecker.getColor(context, unifyR.color.Unify_NN300),
            )
        }

        init {
            binding.tvOriginalPrice.paintFlags =
                binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        fun bind(item: PlayProductUiModel.Product) {
            binding.imgProduct.loadImage(item.imageUrl)
            binding.tvName.text = item.title
            binding.labelOos.showWithCondition(item.stock == OutOfStock)

            binding.btnAtc.setDrawable(
                if (item.stock is StockAvailable) iconCartEnabled else iconCartDisabled,
                UnifyButton.DrawablePosition.RIGHT,
            )

            binding.viewOverlayOos.showWithCondition(item.stock == OutOfStock)

            binding.btnAtc.isEnabled = item.stock is StockAvailable
            binding.btnBuy.isEnabled = item.stock is StockAvailable

            binding.btnAtc.showWithCondition(item.stock != ComingSoon)
            binding.btnBuy.showWithCondition(item.stock != ComingSoon)

            when (item.price) {
                is DiscountedPrice -> {
                    binding.tvDiscount.visibility = View.VISIBLE
                    binding.tvDiscount.text = context.getString(
                        R.string.play_discount_percent,
                        item.price.discountPercent,
                    )
                    binding.tvOriginalPrice.visibility = View.VISIBLE
                    binding.tvOriginalPrice.text = item.price.originalPrice
                    binding.tvPrice.text = item.price.discountedPrice
                }
                is OriginalPrice -> {
                    binding.tvDiscount.visibility = View.GONE
                    binding.tvOriginalPrice.visibility = View.GONE
                    binding.tvPrice.text = item.price.price
                }
            }
            binding.tvInfo.text = getInfo(item)

            binding.btnAtc.setOnClickListener {
                listener.onAtcClicked(this, item)
            }
            binding.btnBuy.setOnClickListener {
                listener.onBuyClicked(this, item)
            }
            binding.root.setOnClickListener {
                listener.onClicked(this, item)
            }
        }

        private fun getInfo(item: PlayProductUiModel.Product): CharSequence {
            return buildSpannedString {
                append(getString(R.string.play_product_pinned))

                if (item.stock !is StockAvailable ||
                    item.stock.stock > MIN_STOCK) return@buildSpannedString

                append(' ')
                val separator = getString(R.string.play_product_pinned_info_separator)
                append(separator, separatorSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

                append(' ')
                val stockText = getString(R.string.play_product_item_stock, item.stock.stock)
                append(stockText, stockSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }

        companion object {
            private const val MIN_STOCK = 5

            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = PinnedProduct(
                ItemPlayPinnedProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                listener,
            )
        }

        interface Listener {
            fun onClicked(viewHolder: PinnedProduct, product: PlayProductUiModel.Product)
            fun onAtcClicked(viewHolder: PinnedProduct, product: PlayProductUiModel.Product)
            fun onBuyClicked(viewHolder: PinnedProduct, product: PlayProductUiModel.Product)
        }
    }
}