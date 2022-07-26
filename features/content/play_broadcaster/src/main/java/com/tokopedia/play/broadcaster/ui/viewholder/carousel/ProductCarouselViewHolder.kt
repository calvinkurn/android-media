package com.tokopedia.play.broadcaster.ui.viewholder.carousel

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemPlayBroPlaceholderCarouselBinding
import com.tokopedia.play.broadcaster.databinding.ItemPlayBroProductCarouselBinding
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.pinnedproduct.PinStatus
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.unifyprinciples.R as unifyR

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

            binding.viewPinProduct.root.showWithCondition(item.pinStatus.canPin)
            binding.viewPinProduct.ivLoaderPin.showWithCondition(item.pinStatus.isLoading)
            binding.viewPinProduct.ivPin.showWithCondition(!item.pinStatus.isLoading)
            binding.viewPinProduct.root.setOnClickListener {
                listener.onPinProductClicked(item)
            }

            when(item.pinStatus.pinStatus) {
                PinStatus.Pinned -> {
                    binding.viewPinProduct.ivPin.setImage(newIconId = IconUnify.PUSH_PIN, newDarkEnable = MethodChecker.getColor(context, unifyR.color.Unify_RN400), newLightEnable = MethodChecker.getColor(context, unifyR.color.Unify_RN400))
                    binding.viewPinProduct.tvPin.text = context.resources.getString(R.string.play_bro_unpin)
                    binding.viewPinProduct.tvPin.setTextColor(MethodChecker.getColor(context, unifyR.color.Unify_RN400))
                }
                PinStatus.Unpin -> {
                    binding.viewPinProduct.ivPin.setImage(newIconId = IconUnify.PUSH_PIN, newDarkEnable = MethodChecker.getColor(context, unifyR.color.Unify_Static_White), newLightEnable = MethodChecker.getColor(context, unifyR.color.Unify_Static_White))
                    binding.viewPinProduct.tvPin.text = context.resources.getString(R.string.play_bro_pin)
                    binding.viewPinProduct.tvPin.setTextColor(MethodChecker.getColor(context, unifyR.color.Unify_Static_White))
                }
            }
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
            fun onPinProductClicked(product: ProductUiModel)
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