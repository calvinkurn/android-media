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
            binding.bind(item.product)
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
            binding.viewProduct.bind(item.product)
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

fun ItemMyShopProductListBinding.bind(product: ProductUiModel) {
    imgProduct.loadImage(product.coverURL)
    tvName.text = product.name
    tvStock.text = root.context.getString(
        R.string.cc_product_stock_template, product.stock
    )

    if(product.isDiscount) {
        tvPrice.text = product.priceFmt
        labelDiscountPercentage.text = product.discountFmt
        tvPriceBeforeDiscount.text = product.priceOriginalFmt
        llDiscount.visibility = View.VISIBLE
    } else {
        tvPrice.text = product.priceFmt
        llDiscount.visibility = View.GONE
    }
}