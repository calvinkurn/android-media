package com.tokopedia.play.ui.productfeatured.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ItemPlayProductFeaturedBinding
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play_common.view.loadImage

/**
 * Created by jegul on 23/02/21
 */
class ProductFeaturedViewHolder(
    private val binding: ItemPlayProductFeaturedBinding,
    private val listener: ProductBasicViewHolder.Listener
) : BaseViewHolder(binding.root) {
    private val context: Context
        get() = binding.root.context

    init {
        binding.tvSlashedPrice.paintFlags =
            binding.tvSlashedPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    @SuppressLint("ResourceType")
    fun bind(item: PlayProductUiModel.Product) {
        binding.ivProductImage.loadImage(item.imageUrl)

        when (item.price) {
            is DiscountedPrice -> {
                binding.tvProductDiscount.visibility = View.VISIBLE
                binding.tvProductDiscount.text = context.getString(
                    R.string.play_discount_percent,
                    item.price.discountPercent
                )
                binding.tvSlashedPrice.visibility = View.VISIBLE
                binding.tvFinalPrice.text = item.price.discountedPrice
                binding.tvSlashedPrice.text = item.price.originalPrice
            }
            is OriginalPrice -> {
                binding.tvProductDiscount.visibility = View.GONE
                binding.tvSlashedPrice.visibility = View.INVISIBLE
                binding.tvFinalPrice.text = item.price.price
            }
        }

        binding.rclPlayCarouselCard.setOnClickListener {
            if (item.applink.isNullOrEmpty()) return@setOnClickListener
            listener.onClickProductCard(item, bindingAdapterPosition)
        }

        binding.lblProductNumber.showWithCondition(item.isNumerationShown)
        binding.lblProductNumber.text = item.number

        binding.layoutRibbon.showWithCondition(item.label.rankFmt.isNotBlank())
        binding.layoutRibbon.rankFmt = item.label.rankFmt
        binding.layoutRibbon.configRibbon(item.label.rankColors)
    }

    fun startAnimation() {
        binding.layoutRibbon.startAnimation()
    }
    companion object {
        fun create(
            parent: ViewGroup,
            listener: ProductBasicViewHolder.Listener
        ) = ProductFeaturedViewHolder(
            ItemPlayProductFeaturedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }
}
