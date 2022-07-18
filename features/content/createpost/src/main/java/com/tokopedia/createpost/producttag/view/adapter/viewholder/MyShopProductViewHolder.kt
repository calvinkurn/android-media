package com.tokopedia.createpost.producttag.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.createpost.databinding.ItemMyShopProductListBinding
import com.tokopedia.createpost.producttag.view.adapter.MyShopProductAdapter
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.kotlin.extensions.view.loadImage

/**
 * Created By : Jonathan Darwin on May 09, 2022
 */
internal class MyShopProductViewHolder private constructor() {

    internal class Product(
        private val binding: ItemMyShopProductListBinding,
        private val onSelected: (ProductUiModel, Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyShopProductAdapter.Model.Product) {
            binding.imgProduct.loadImage(item.product.coverURL)
            binding.tvName.text = item.product.name
            binding.tvStock.text = itemView.context.getString(
                R.string.cc_product_stock_template, item.product.stock
            )

            if(item.product.isDiscount) {
                binding.tvPrice.text = item.product.priceFmt
                binding.labelDiscountPercentage.text = item.product.discountFmt
                binding.tvPriceBeforeDiscount.text = item.product.priceOriginalFmt
                binding.llDiscount.visibility = View.VISIBLE
            } else {
                binding.tvPrice.text = item.product.priceFmt
                binding.llDiscount.visibility = View.GONE
            }

            binding.root.setOnClickListener { onSelected(item.product, adapterPosition) }
        }

        companion object {

            fun create(
                parent: ViewGroup,
                onSelected: (ProductUiModel, Int) -> Unit
            ) = Product(
                binding = ItemMyShopProductListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onSelected = onSelected,
            )
        }
    }
}