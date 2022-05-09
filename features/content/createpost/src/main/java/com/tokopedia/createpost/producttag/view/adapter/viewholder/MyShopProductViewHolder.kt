package com.tokopedia.createpost.producttag.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.createpost.createpost.databinding.ItemMyShopProductListBinding
import com.tokopedia.createpost.createpost.databinding.ItemProductTagLoadingListBinding
import com.tokopedia.createpost.producttag.view.adapter.MyShopProductAdapter
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on May 09, 2022
 */
internal class MyShopProductViewHolder private constructor() {

    internal class Product(
        private val binding: ItemMyShopProductListBinding,
        private val onSelected: (ProductUiModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyShopProductAdapter.Model.Product) {
            binding.productView.apply {
                setProductModel(item.product.toProductCard())
                setOnClickListener { onSelected(item.product) }
            }
        }

        companion object {

            fun create(
                parent: ViewGroup,
                onSelected: (ProductUiModel) -> Unit
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

    internal class Loading(
        binding: ItemProductTagLoadingListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            val layoutParams = itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }

        companion object {

            fun create(parent: ViewGroup) = Loading(
                ItemProductTagLoadingListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )
        }
    }
}