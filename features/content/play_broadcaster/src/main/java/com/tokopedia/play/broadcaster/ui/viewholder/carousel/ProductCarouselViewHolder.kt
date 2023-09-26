package com.tokopedia.play.broadcaster.ui.viewholder.carousel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.content.product.picker.R as contentproductpickerR
import com.tokopedia.play.broadcaster.databinding.ItemPlayBroPinnedProductCarouselBinding
import com.tokopedia.play.broadcaster.databinding.ItemPlayBroPlaceholderCarouselBinding
import com.tokopedia.play.broadcaster.databinding.ItemPlayBroProductCarouselBinding
import com.tokopedia.content.product.picker.sgc.model.DiscountedPrice
import com.tokopedia.content.product.picker.sgc.model.OriginalPrice
import com.tokopedia.content.product.picker.sgc.model.product.ProductUiModel
import com.tokopedia.play_common.view.loadImage

/**
 * Created by kenny.hadisaputra on 09/02/22
 */
class ProductCarouselViewHolder private constructor() {

    class Product(
        private val binding: ItemPlayBroProductCarouselBinding,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context: Context
            get() = itemView.context

        init {
            binding.tvProductTagNormalPrice.paintFlags =
                binding.tvProductTagNormalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        @SuppressLint("ResourceType")
        fun bind(item: ProductUiModel) {
            binding.ivProductTag.setImageUrl(item.imageUrl)
            binding.tvProductTagEmptyStock.showWithCondition(item.stock.isLessThanEqualZero())
            binding.ivProductTagCover.showWithCondition(item.stock.isLessThanEqualZero())

            when (val price = item.price) {
                is DiscountedPrice -> {
                    binding.tvProductTagNormalPrice.show()
                    binding.tvProductTagDiscount.show()
                    binding.tvProductTagDiscount.text = context.getString(
                        contentproductpickerR.string.play_bro_product_discount_template,
                        price.discountPercent
                    )
                    binding.tvProductTagPrice.text = price.discountedPrice
                    binding.tvProductTagNormalPrice.text = price.originalPrice
                }
                is OriginalPrice -> {
                    binding.tvProductTagNormalPrice.invisible()
                    binding.tvProductTagDiscount.hide()
                    binding.tvProductTagPrice.text = price.price
                }
                else -> {
                    binding.tvProductTagNormalPrice.invisible()
                    binding.tvProductTagDiscount.hide()
                    binding.tvProductTagPrice.text = context.getString(
                        contentproductpickerR.string.play_bro_product_tag_no_price
                    )
                }
            }
            binding.viewPinProduct.showWithCondition(item.pinStatus.canPin)
            binding.viewPinProduct.setupPinned(item.pinStatus.isPinned, item.pinStatus.isLoading)
            binding.viewPinProduct.setOnClickListener {
                listener.onPinClicked(item)
            }
            binding.tvProductTagNumber.text = item.number
        }

        companion object {
            fun create(parent: ViewGroup, listener: Listener): Product {
                return Product(
                    ItemPlayBroProductCarouselBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                    listener
                )
            }
        }

        interface Listener {
            fun onPinClicked(product: ProductUiModel)
        }
    }

    class PinnedProduct(
        private val binding: ItemPlayBroPinnedProductCarouselBinding,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvPinnedProductCarouselOriginalPrice.paintFlags =
                binding.tvPinnedProductCarouselOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        private val context: Context
            get() = itemView.context


        @SuppressLint("ResourceType")
        fun bind(item: ProductUiModel) {
            binding.ivPinnedProductCarousel.loadImage(item.imageUrl)
            binding.tvPinnedProductCarouselName.text = item.name
            binding.ivPinnedProductCarousel.showWithCondition(item.stock.isMoreThanZero())
            binding.ivPinnedProductCarouselOos.showWithCondition(item.stock.isLessThanEqualZero())
            binding.tvProductSummaryEmptyStock.showWithCondition(item.stock.isLessThanEqualZero())

            when (val price = item.price) {
                is OriginalPrice -> {
                    binding.tvPinnedProductCarouselPrice.text = price.price
                    binding.labelPinnedProductCarouselDiscount.visibility = View.GONE
                    binding.tvPinnedProductCarouselOriginalPrice.visibility = View.GONE
                }
                is DiscountedPrice -> {
                    binding.tvPinnedProductCarouselPrice.text = price.discountedPrice
                    binding.tvPinnedProductCarouselOriginalPrice.text = price.originalPrice
                    binding.labelPinnedProductCarouselDiscount.text = itemView.context.getString(
                        contentproductpickerR.string.play_bro_product_discount_template,
                        price.discountPercent
                    )
                    binding.labelPinnedProductCarouselDiscount.visible()
                    binding.tvPinnedProductCarouselOriginalPrice.visible()
                }
                else -> {
                    binding.tvPinnedProductCarouselPrice.text = ""
                    binding.labelPinnedProductCarouselDiscount.visible()
                    binding.tvPinnedProductCarouselOriginalPrice.visible()
                }
            }

            binding.viewPinProduct.showWithCondition(item.pinStatus.canPin)
            if (item.pinStatus.isPinned) listener.onImpressPinnedProduct(item)
            binding.viewPinProduct.setupPinned(item.pinStatus.isPinned, item.pinStatus.isLoading)
            binding.viewPinProduct.setOnClickListener {
                listener.onPinClicked(item)
            }
            binding.tvPinnedProductTagNumber.text = item.number
        }

        companion object {
            fun create(parent: ViewGroup, listener: Listener): PinnedProduct =
                PinnedProduct(
                    ItemPlayBroPinnedProductCarouselBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                    listener
                )
        }

        interface Listener {
            fun onPinClicked(product: ProductUiModel)
            fun onImpressPinnedProduct(product: ProductUiModel)
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
