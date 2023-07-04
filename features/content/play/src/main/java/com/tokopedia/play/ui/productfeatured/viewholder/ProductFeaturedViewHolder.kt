package com.tokopedia.play.ui.productfeatured.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ItemPlayProductFeaturedBinding
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play_common.view.loadImage
import java.lang.Exception

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

        binding.root.setOnClickListener {
            if (item.applink.isNullOrEmpty()) return@setOnClickListener
            listener.onClickProductCard(item, adapterPosition)
        }

        binding.lblProductNumber.showWithCondition(item.isNumerationShown)
        binding.lblProductNumber.text = item.number

        configRibbon(colors = item.rankColors, rankFmt = item.rankFmt)
    }

    private fun configRibbon(colors: List<String>, rankFmt: String) {
        binding.layoutRibbon.root.showWithCondition(rankFmt.isNotBlank())
        binding.layoutRibbon.playTvRibbon.text = rankFmt

        if (colors.isNullOrEmpty()) return

        try {
            binding.layoutRibbon.ivTailRibbon.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(colors.first()), BlendModeCompat.SRC_ATOP)
            binding.layoutRibbon.ivBackRibbon.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(colors.getOrElse(1) {colors.first()} ), BlendModeCompat.SRC_ATOP)
        } catch (e: Exception) { false }
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
