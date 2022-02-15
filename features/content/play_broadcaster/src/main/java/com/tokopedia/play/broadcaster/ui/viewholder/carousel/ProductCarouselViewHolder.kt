package com.tokopedia.play.broadcaster.ui.viewholder.carousel

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemPlayBroPlaceholderCarouselBinding
import com.tokopedia.play.broadcaster.databinding.ItemPlayBroProductCarouselBinding
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel

/**
 * Created by kenny.hadisaputra on 09/02/22
 */
internal class ProductCarouselViewHolder private constructor() {

    class Product(
        private val binding: ItemPlayBroProductCarouselBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context: Context
            get() = itemView.context

        init {
            binding.tvProductTagNormalPrice.paintFlags =
                binding.tvProductTagNormalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        fun bind(item: ProductUiModel) {
            binding.ivProductTag.setImageUrl(item.imageUrl)
            if (item.stock > 0) {
                binding.tvProductTagStock.text = context.getString(
                    R.string.play_bro_product_stock,
                    item.stock
                )
                binding.ivProductTagCover.hide()
            } else {
                binding.tvProductTagStock.text = context.getString(
                    R.string.play_bro_product_tag_stock_empty
                )
                binding.ivProductTagCover.show()
            }

            when(item.price) {
                is DiscountedPrice -> {
                    binding.tvProductTagNormalPrice.show()
                    binding.tvProductTagDiscount.show()
                    binding.tvProductTagDiscount.text = context.getString(R.string.play_bro_product_discount_template, item.price.discountPercent)
                    binding.tvProductTagPrice.text = item.price.discountedPrice
                    binding.tvProductTagNormalPrice.text = item.price.originalPrice
                }
                is OriginalPrice -> {
                    binding.tvProductTagNormalPrice.invisible()
                    binding.tvProductTagDiscount.hide()
                    binding.tvProductTagPrice.text = item.price.price
                }
                else -> {
                    binding.tvProductTagNormalPrice.invisible()
                    binding.tvProductTagDiscount.hide()
                    binding.tvProductTagPrice.text = context.getString(
                        R.string.play_bro_product_tag_no_price
                    )
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): Product {
                return Product(
                    ItemPlayBroProductCarouselBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                )
            }
        }
    }

    class Loading(
        binding: ItemPlayBroPlaceholderCarouselBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): Loading {
                return Loading(
                    ItemPlayBroPlaceholderCarouselBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                )
            }
        }
    }
}