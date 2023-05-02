package com.tokopedia.search.result.product.emptystate

import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import androidx.annotation.DimenRes
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

    private val resources: Resources?
        get() = itemView.context?.resources

    private val displayMetrics: DisplayMetrics?
        get() = resources?.displayMetrics

    private fun getDimensionPixelSize(@DimenRes id: Int): Int {
        return resources?.getDimensionPixelSize(id) ?: 0
    }

    override fun bind(model: EmptyStateKeywordDataView) {
        bindNoResultImage(model)
        bindTitleTextView(model)
        bindContentTextView(model)
        bindNewSearchButton()
        bindGlobalSearchButton(model)
    }

    private fun bindNoResultImage(model: EmptyStateKeywordDataView) {
        binding?.noResultImage?.setImageResource(
            com.tokopedia.resources.common.R.drawable.ic_product_search_not_found
        )

        binding?.noResultImage?.setImageSize(model)
    }

    private fun ImageView.setImageSize(model: EmptyStateKeywordDataView) {
        val layoutParams = layoutParams ?: return

        val (width, height) = getImageSize(model)
        layoutParams.width = width
        layoutParams.height = height

        this.layoutParams = layoutParams
    }

    private fun getImageSize(model: EmptyStateKeywordDataView): Pair<Int, Int> =
        if (model.isShowAdsLowOrganic)
            Pair(
                getDimensionPixelSize(com.tokopedia.search.R.dimen.search_empty_state_image_show_ads_width),
                getDimensionPixelSize(com.tokopedia.search.R.dimen.search_empty_state_image_show_ads_height),
            )
        else
            Pair(
                getDimensionPixelSize(com.tokopedia.search.R.dimen.search_empty_state_image_default_width),
                getDimensionPixelSize(com.tokopedia.search.R.dimen.search_empty_state_image_default_height),
            )

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
