package com.tokopedia.search.result.product.emptystate

import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchEmptyStateKeywordProductBinding
import com.tokopedia.utils.view.binding.viewBinding

class EmptyStateKeywordViewHolder(
    view: View,
    private val emptyStateListener: EmptyStateListener,
) : AbstractViewHolder<EmptyStateKeywordDataView>(view) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = com.tokopedia.search.R.layout.search_empty_state_keyword_product
    }

    private var binding: SearchEmptyStateKeywordProductBinding? by viewBinding()

    override fun bind(model: EmptyStateKeywordDataView) {
        bindNoResultImage()
        bindTitleTextView(model)
        bindContentTextView(model)
        bindNewSearchButton()
        bindGlobalSearchButton(model)
    }

    private fun bindNoResultImage() {
        binding?.noResultImage?.setImageResource(
            com.tokopedia.resources.common.R.drawable.ic_product_search_not_found
        )
    }

    private fun bindTitleTextView(emptyStateDataView: EmptyStateKeywordDataView) {
        binding?.textViewEmptyTitleText?.text = emptyTitleText(emptyStateDataView)
    }

    private fun emptyTitleText(emptyStateDataView: EmptyStateKeywordDataView) =
        if (emptyStateDataView.isLocalSearch)
            getString(R.string.msg_empty_search_product_title_local)
        else
            getString(R.string.msg_empty_search_product_title)

    private fun bindContentTextView(emptyStateDataView: EmptyStateKeywordDataView) {
        binding?.textViewEmptyContentText?.text = emptyContentText(emptyStateDataView)
    }

    private fun emptyContentText(emptyStateDataView: EmptyStateKeywordDataView) =
        when {
            emptyStateDataView.isLocalSearch ->
                getString(
                    R.string.msg_empty_search_product_content_local,
                    emptyStateDataView.keyword,
                    emptyStateDataView.pageTitle,
                )
            else ->
                getString(R.string.msg_empty_search_product_content)
        }

    private fun bindNewSearchButton() {
        val binding = binding ?: return

        binding.buttonChangeKeyword.setOnClickListener {
            emptyStateListener.onEmptyButtonClicked()
        }
    }

    private fun bindGlobalSearchButton(emptyStateDataView: EmptyStateKeywordDataView) {
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