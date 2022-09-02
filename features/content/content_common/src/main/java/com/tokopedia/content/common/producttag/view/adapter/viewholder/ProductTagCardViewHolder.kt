package com.tokopedia.content.common.producttag.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.*
import com.tokopedia.content.common.producttag.view.adapter.ProductTagCardAdapter
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.unifycomponents.ticker.TickerCallback

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
            binding.tvSuggestion.setOnClickListener {
                item.onSuggestionClicked()
            }
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

    internal class Ticker(
        private val binding: ItemGlobalSearchTickerListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            val layoutParams = itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }

        fun bind(item: ProductTagCardAdapter.Model.Ticker) {
            binding.ticker.setHtmlDescription(item.text)
            binding.ticker.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    item.onTickerClicked()
                }

                override fun onDismiss() {
                    item.onTickerClosed()
                }
            })
        }

        companion object {

            fun create(parent: ViewGroup) = Ticker(
                ItemGlobalSearchTickerListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )
        }
    }

    internal class Product(
        private val binding: ItemProductTagCardListBinding,
        private val onSelected: (ProductUiModel, Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductTagCardAdapter.Model.Product) {
            binding.productView.apply {
                setProductModel(item.product.toProductCard())
                setOnClickListener { onSelected(item.product, adapterPosition) }
            }
        }

        companion object {

            fun create(
                parent: ViewGroup,
                onSelected: (ProductUiModel, Int) -> Unit
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

    internal class ProductWithCheckbox(
        private val binding: ItemProductTagCardWithCheckboxListBinding,
        private val onSelected: (ProductUiModel, Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductTagCardAdapter.Model.ProductWithCheckbox) {
            binding.productView.apply {
                setProductModel(item.product.toProductCard())
                setOnClickListener { onSelected(item.product, adapterPosition) }
            }

            binding.checkboxProduct.isChecked = item.isSelected
        }

        companion object {

            fun create(
                parent: ViewGroup,
                onSelected: (ProductUiModel, Int) -> Unit
            ) = ProductWithCheckbox(
                binding = ItemProductTagCardWithCheckboxListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onSelected = onSelected,
            )
        }
    }

    internal class EmptyState(
        private val binding: ItemGlobalSearchEmptyStateListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            val layoutParams = itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }

        fun bind(item: ProductTagCardAdapter.Model.EmptyState) {
            val context = itemView.context
            binding.emptyState.apply {
                setImageUrl(context.getString(R.string.img_search_no_product))
                setPrimaryCTAText("")
                setSecondaryCTAText("")


                if(item.hasFilterApplied) {
                    setTitle(context.getString(R.string.cc_global_search_product_filter_not_found_title))
                    setDescription(context.getString(R.string.cc_global_search_product_filter_not_found_desc))
                    setOrientation(EmptyStateUnify.Orientation.VERTICAL)
                }
                else {
                    setTitle(context.getString(R.string.cc_global_search_product_query_not_found_title))
                    setDescription(context.getString(R.string.cc_global_search_product_query_not_found_desc))
                    setOrientation(EmptyStateUnify.Orientation.HORIZONTAL)
                }
            }
        }

        companion object {

            fun create(parent: ViewGroup) = EmptyState(
                ItemGlobalSearchEmptyStateListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )
        }
    }
}