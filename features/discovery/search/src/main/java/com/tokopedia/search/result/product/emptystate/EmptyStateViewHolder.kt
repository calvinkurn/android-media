package com.tokopedia.search.result.product.emptystate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.LAYOUT_DIRECTION_LTR
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchEmptyStateProductBinding
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.SpacingItemDecoration
import com.tokopedia.utils.view.binding.viewBinding

class EmptyStateViewHolder(
    view: View,
    private val emptyStateListener: EmptyStateListener,
) : AbstractViewHolder<EmptyStateDataView>(view) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = com.tokopedia.search.R.layout.search_empty_state_product
    }

    private var binding: SearchEmptyStateProductBinding? by viewBinding()
    private var productSelectedFilterAdapter: ProductSelectedFilterAdapter? = null

    init {
        initSelectedFilterRecyclerView()
    }

    private fun initSelectedFilterRecyclerView() {
        val binding = binding ?: return

        val layoutManager = ChipsLayoutManager.newBuilder(itemView.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        val staticDimen8dp = itemView.context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)

        binding.selectedFilterRecyclerView.addItemDecoration(SpacingItemDecoration(staticDimen8dp))
        binding.selectedFilterRecyclerView.layoutManager = layoutManager
        ViewCompat.setLayoutDirection(binding.selectedFilterRecyclerView, LAYOUT_DIRECTION_LTR)

        productSelectedFilterAdapter = ProductSelectedFilterAdapter(emptyStateListener)
        binding.selectedFilterRecyclerView.adapter = productSelectedFilterAdapter
    }

    override fun bind(model: EmptyStateDataView) {
        bindNoResultImage()
        bindTitleTextView(model)
        bindContentTextView(model)
        bindNewSearchButton(model)
        bindRecyclerView()
        bindGlobalSearchButton(model)
    }

    private fun bindNoResultImage() {
        binding?.noResultImage?.setImageResource(
            com.tokopedia.resources.common.R.drawable.ic_product_search_not_found
        )
    }

    private fun bindTitleTextView(emptyStateDataView: EmptyStateDataView) {
        binding?.textViewEmptyTitleText?.text = emptyTitleText(emptyStateDataView)
    }

    private fun emptyTitleText(emptyStateDataView: EmptyStateDataView) =
        if (emptyStateDataView.isLocalSearch)
            getString(R.string.msg_empty_search_product_title_local)
        else
            getString(R.string.msg_empty_search_product_title)

    private fun bindContentTextView(emptyStateDataView: EmptyStateDataView) {
        binding?.textViewEmptyContentText?.text = emptyContentText(emptyStateDataView)
    }

    private fun emptyContentText(emptyStateDataView: EmptyStateDataView) =
        when {
            emptyStateDataView.isLocalSearch ->
                getString(
                    R.string.msg_empty_search_product_content_local,
                    emptyStateDataView.keyword,
                    emptyStateDataView.pageTitle,
                )
            emptyStateDataView.isFilterActive ->
                getString(R.string.msg_empty_search_product_content_with_filter)
            else ->
                getString(R.string.msg_empty_search_product_content)
        }

    private fun bindNewSearchButton(emptyStateDataView: EmptyStateDataView) {
        val binding = binding ?: return
        val shouldShow = !emptyStateDataView.isFilterActive

        binding.buttonAddPromo.shouldShowWithAction(shouldShow) {
            binding.buttonAddPromo.setOnClickListener {
                emptyStateListener.onEmptyButtonClicked()
            }
        }
    }

    private fun bindRecyclerView() {
        val binding = binding ?: return
        val selectedFilterList = emptyStateListener.getSelectedFilterAsOptionList() ?: return
        val shouldShowSelectedFilterList = selectedFilterList.isNotEmpty()

        binding.selectedFilterRecyclerView.shouldShowWithAction(shouldShowSelectedFilterList) {
            productSelectedFilterAdapter?.setOptionList(selectedFilterList)
        }
    }

    private fun bindGlobalSearchButton(emptyStateDataView: EmptyStateDataView) {
        val binding = binding ?: return
        val shouldShowButtonToGlobalSearch = emptyStateDataView.isLocalSearch
        val buttonEmptySearchToGlobalSearch = binding.buttonEmptySearchToGlobalSearch

        buttonEmptySearchToGlobalSearch.shouldShowWithAction(shouldShowButtonToGlobalSearch) {
            buttonEmptySearchToGlobalSearch.setOnClickListener {
                val applink = emptyStateDataView.globalSearchApplink
                emptyStateListener.onEmptySearchToGlobalSearchClicked(applink)
            }
        }
    }

    private class ProductSelectedFilterAdapter(
        private val clickListener: EmptyStateListener
    ) : RecyclerView.Adapter<ProductSelectedFilterItemViewHolder>() {

        private val optionList = mutableListOf<Option>()

        fun setOptionList(optionList: List<Option>) {
            this.optionList.clear()
            this.optionList.addAll(optionList)

            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ProductSelectedFilterItemViewHolder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(
                    com.tokopedia.search.R.layout.search_product_empty_state_selected_filter_item,
                    parent,
                    false,
                )

            return ProductSelectedFilterItemViewHolder(view, clickListener)
        }

        override fun onBindViewHolder(holder: ProductSelectedFilterItemViewHolder, position: Int) {
            holder.bind(optionList[position])
        }

        override fun getItemCount(): Int {
            return optionList.size
        }
    }

    private class ProductSelectedFilterItemViewHolder(
        itemView: View,
        private val clickListener: EmptyStateListener
    ) : RecyclerView.ViewHolder(itemView) {

        private val filterText: TextView = itemView.findViewById(com.tokopedia.search.R.id.filter_text)

        fun bind(option: Option) {
            filterText.text = option.name
            filterText.setOnClickListener {
                clickListener.onSelectedFilterRemoved(option.uniqueId)
            }
        }
    }

    private fun getString(@StringRes stringRes: Int, vararg value: String): String {
        return itemView.context.getString(stringRes, value)
    }
}