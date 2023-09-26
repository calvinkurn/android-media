package com.tokopedia.content.product.picker.sgc.view.viewholder

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.content.product.picker.databinding.ItemLoadingBinding
import com.tokopedia.content.product.picker.databinding.ItemProductListBinding
import com.tokopedia.content.product.picker.R
import com.tokopedia.content.product.picker.sgc.model.DiscountedPrice
import com.tokopedia.content.product.picker.sgc.model.OriginalPrice
import com.tokopedia.content.product.picker.sgc.model.product.ProductUiModel
import com.tokopedia.content.product.picker.sgc.view.adapter.ProductListAdapter
import com.tokopedia.play_common.view.loadImage

/**
 * Created by kenny.hadisaputra on 04/02/22
 */
internal class ProductListViewHolder private constructor() {

    internal class Product(
        private val binding: ItemProductListBinding,
        private val onSelected: (ProductUiModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvPriceBeforeDiscount.paintFlags =
                binding.tvPriceBeforeDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        fun bind(item: ProductListAdapter.Model.Product) {
            binding.imgProduct.loadImage(item.product.imageUrl)
            binding.tvName.text = item.product.name
            binding.tvStock.text = itemView.context.getString(
                R.string.play_bro_product_stock, item.product.stock
            )

            binding.checkboxProduct.isChecked = item.isSelected

            when(val productPrice = item.product.price) {
                is OriginalPrice -> {
                    binding.tvPrice.text = productPrice.price
                    binding.llDiscount.visibility = View.GONE
                }
                is DiscountedPrice -> {
                    binding.tvPrice.text = productPrice.discountedPrice
                    binding.labelDiscountPercentage.text = itemView.context.getString(
                        R.string.play_bro_product_discount_template,
                        productPrice.discountPercent
                    )
                    binding.tvPriceBeforeDiscount.text = productPrice.originalPrice
                    binding.llDiscount.visibility = View.VISIBLE
                }
                else -> {
                    binding.tvPrice.text = ""
                    binding.llDiscount.visibility = View.GONE
                }
            }

            if (item.product.stock > 0) {
                binding.tvStock.visibility = View.VISIBLE
                binding.checkboxProduct.visibility = View.VISIBLE
                binding.flEmptyForeground.visibility = View.GONE
                binding.viewEmptyStock.root.visibility = View.GONE
            } else {
                binding.tvStock.visibility = View.GONE
                binding.checkboxProduct.visibility = View.GONE
                binding.flEmptyForeground.visibility = View.VISIBLE
                binding.viewEmptyStock.root.visibility = View.VISIBLE
            }

            itemView.setOnClickListener { onSelected(item.product) }
        }

        companion object {

            fun create(
                parent: ViewGroup,
                onSelected: (ProductUiModel) -> Unit
            ) = Product(
                binding = ItemProductListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onSelected = onSelected,
            )
        }
    }

    internal class Loading(
        binding: ItemLoadingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            val layoutParams = itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }

        companion object {

            fun create(parent: ViewGroup) = Loading(
                ItemLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )
        }
    }
}
