package com.tokopedia.search.result.product.emptystate

import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.LAYOUT_DIRECTION_LTR
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
    private var productSelectedFilterAdapter: EmptyStateSelectedFilterAdapter? = null

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

        productSelectedFilterAdapter = EmptyStateSelectedFilterAdapter(emptyStateListener)
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

    private fun getString(@StringRes stringRes: Int, vararg value: String): String {
        return itemView.context.getString(stringRes, *value)
    }
}