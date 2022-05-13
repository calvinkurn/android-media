package com.tokopedia.createpost.producttag.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.createpost.createpost.databinding.ItemGlobalSearchSuggestionListBinding
import com.tokopedia.createpost.createpost.databinding.ItemProductTagCardListBinding
import com.tokopedia.createpost.createpost.databinding.ItemProductTagLoadingListBinding
import com.tokopedia.createpost.producttag.view.adapter.ProductTagCardAdapter
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
internal class ProductTagCardViewHolder private constructor() {

    internal class Suggestion(
        private val binding: ItemGlobalSearchSuggestionListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            val layoutParams = itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }

        fun bind(item: ProductTagCardAdapter.Model.Suggestion) {
            binding.tvSuggestion.text = HtmlCompat.fromHtml(item.text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        companion object {

            fun create(parent: ViewGroup) = Suggestion(
                ItemGlobalSearchSuggestionListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )
        }
    }

    internal class Product(
        private val binding: ItemProductTagCardListBinding,
        private val onSelected: (ProductUiModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductTagCardAdapter.Model.Product) {
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
                binding = ItemProductTagCardListBinding.inflate(
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