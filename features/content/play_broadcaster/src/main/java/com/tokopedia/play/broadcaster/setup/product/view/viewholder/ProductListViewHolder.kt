package com.tokopedia.play.broadcaster.setup.product.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemLoadingBinding
import com.tokopedia.play.broadcaster.databinding.ItemProductListBinding
import com.tokopedia.play.broadcaster.setup.product.view.adapter.ProductListAdapter
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
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
            itemView.setOnClickListener {
                binding.checkboxProduct.isChecked = !binding.checkboxProduct.isChecked
            }
        }

        fun bind(item: ProductListAdapter.Model.Product) {
            binding.imgProduct.loadImage(item.product.imageUrl)
            binding.tvName.text = item.product.name
            binding.tvStock.text = itemView.context.getString(
                R.string.play_bro_product_chooser_stock, item.product.stock
            )

            setCheckboxManually(item)

            when(item.product.price) {
                is OriginalPrice -> {
                    binding.tvPrice.text = item.product.price.price
                    binding.llDiscount.visibility = View.GONE
                }
                is DiscountedPrice -> {
                    binding.tvPrice.text = item.product.price.originalPrice
                    binding.labelDiscountPercentage.text = itemView.context.getString(
                        R.string.play_bro_product_discount_template,
                        item.product.price.discountPercent
                    )
                    binding.tvDiscountPrice.text = item.product.price.discountedPrice
                    binding.llDiscount.visibility = View.VISIBLE
                }
                else -> {
                    binding.tvPrice.text = ""
                    binding.llDiscount.visibility = View.GONE
                }
            }
        }

        private fun setCheckboxManually(item: ProductListAdapter.Model.Product) {
            binding.checkboxProduct.setOnCheckedChangeListener(null)
            binding.checkboxProduct.isChecked = item.isSelected
            binding.checkboxProduct.setOnCheckedChangeListener { _, _ -> onSelected(item.product) }
        }
    }

    internal class Loading(
        binding: ItemLoadingBinding,
        private val onLoading: () -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            val layoutParams = itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }

        fun bind() {
            onLoading()
        }
    }
}