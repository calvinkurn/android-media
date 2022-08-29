package com.tokopedia.content.common.producttag.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.ItemMyShopProductListBinding
import com.tokopedia.content.common.databinding.ItemMyShopProductWithCheckboxListBinding
import com.tokopedia.content.common.producttag.view.adapter.MyShopProductAdapter
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.media.loader.loadImage

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

    internal class ProductWithCheckbox(
        private val binding: ItemMyShopProductWithCheckboxListBinding,
        private val onSelected: (ProductUiModel, Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyShopProductAdapter.Model.ProductWithCheckbox) {
            binding.viewProduct.imgProduct.loadImage(item.product.coverURL)
            binding.viewProduct.tvName.text = item.product.name
            binding.viewProduct.tvStock.text = itemView.context.getString(
                R.string.cc_product_stock_template, item.product.stock
            )

            if(item.product.isDiscount) {
                binding.viewProduct.tvPrice.text = item.product.priceFmt
                binding.viewProduct.labelDiscountPercentage.text = item.product.discountFmt
                binding.viewProduct.tvPriceBeforeDiscount.text = item.product.priceOriginalFmt
                binding.viewProduct.llDiscount.visibility = View.VISIBLE
            } else {
                binding.viewProduct.tvPrice.text = item.product.priceFmt
                binding.viewProduct.llDiscount.visibility = View.GONE
            }

            binding.root.setOnClickListener { onSelected(item.product, adapterPosition) }

            binding.checkboxProduct.isChecked = item.isSelected
        }

        companion object {

            fun create(
                parent: ViewGroup,
                onSelected: (ProductUiModel, Int) -> Unit
            ) = ProductWithCheckbox(
                binding = ItemMyShopProductWithCheckboxListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onSelected = onSelected,
            )
        }
    }
}