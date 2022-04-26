package com.tokopedia.createpost.producttag.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.createpost.createpost.databinding.ItemLastTaggedProductListBinding
import com.tokopedia.createpost.createpost.databinding.ItemProductTagLoadingListBinding
import com.tokopedia.createpost.producttag.view.adapter.LastTaggedProductAdapter
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
internal class LastTaggedProductViewHolder private constructor() {

    internal class Product(
        private val binding: ItemLastTaggedProductListBinding,
        private val onSelected: (ProductUiModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
//            binding.tvPriceBeforeDiscount.paintFlags =
//                binding.tvPriceBeforeDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        fun bind(item: LastTaggedProductAdapter.Model.Product) {
            /** TODO: handle this */
        }

        companion object {

            fun create(
                parent: ViewGroup,
                onSelected: (ProductUiModel) -> Unit
            ) = Product(
                binding = ItemLastTaggedProductListBinding.inflate(
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