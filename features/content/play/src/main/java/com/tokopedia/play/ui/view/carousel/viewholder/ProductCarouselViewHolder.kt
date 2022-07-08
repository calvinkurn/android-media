package com.tokopedia.play.ui.view.carousel.viewholder

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.databinding.ItemPlayPinnedProductBinding
import com.tokopedia.play.databinding.ItemPlayProductFeaturedBinding
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.ui.productfeatured.viewholder.ProductFeaturedViewHolder
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.R
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

        init {
            binding.btnAtc.setDrawable(
                getIconUnifyDrawable(
                    context,
                    IconUnify.CART,
                    MethodChecker.getColor(context, unifyR.color.Unify_GN500),
                ),
                UnifyButton.DrawablePosition.RIGHT,
            )

            binding.tvOriginalPrice.paintFlags =
                binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        fun bind(item: PlayProductUiModel.Product) {
            binding.imgProduct.loadImage(item.imageUrl)
            binding.tvName.text = item.title

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

        companion object {
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