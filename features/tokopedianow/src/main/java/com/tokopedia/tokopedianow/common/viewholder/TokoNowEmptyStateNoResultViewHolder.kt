package com.tokopedia.tokopedianow.common.viewholder

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper.EXCLUDE_PREFIX
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper.combinePriceFilterIfExists
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategoryEmptyProductBinding
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategoryEmptyStateChipBinding
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowEmptyStateNoResultViewHolder(
        itemView: View,
        private val tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultListener? = null,
): AbstractViewHolder<TokoNowEmptyStateNoResultUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_empty_product
    }

    private var binding: ItemTokopedianowSearchCategoryEmptyProductBinding? by viewBinding()

    private val layoutManager = ChipsLayoutManager
            .newBuilder(itemView.context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()

    override fun bind(element: TokoNowEmptyStateNoResultUiModel?) {
        element ?: return

        val hasActiveFilter = !element.activeFilterList.isNullOrEmpty()

        bindTitle(hasActiveFilter, element)
        bindDescription(hasActiveFilter, element)
        bindFilterList(hasActiveFilter, element)
        bindGoToGlobalSearchButton(hasActiveFilter, element)
        bindChangeKeywordButton(hasActiveFilter)
    }

    private fun bindTitle(hasActiveFilter: Boolean, element: TokoNowEmptyStateNoResultUiModel) {
        val defaultTitle = element.defaultTitle
        val defaultTitleResId = element.defaultTitleResId

        val title = when {
            defaultTitle.isNotEmpty() -> defaultTitle
            defaultTitleResId != null -> getString(defaultTitleResId)
            hasActiveFilter -> getString(R.string.tokopedianow_empty_product_filter_title)
            else -> getString(R.string.tokopedianow_empty_product_title)
        }

        binding?.tokonowEmptyProductTitle?.text = title
    }

    private fun bindDescription(hasActiveFilter: Boolean, element: TokoNowEmptyStateNoResultUiModel) {
        val defaultDescription = element.defaultDescription
        val defaultDescriptionResId = element.defaultDescriptionResId

        val description = when {
            defaultDescription.isNotEmpty() -> defaultDescription
            defaultDescriptionResId != null -> getString(defaultDescriptionResId)
            hasActiveFilter -> getString(R.string.tokopedianow_empty_product_filter_description)
            else -> getString(R.string.tokopedianow_empty_product_description)
        }

        binding?.tokonowEmptyProductDescription?.text = description
    }

    private fun bindFilterList(hasActiveFilter: Boolean, element: TokoNowEmptyStateNoResultUiModel) {
        val filterList = binding?.tokonowEmptyProductFilterList ?: return

        filterList.shouldShowWithAction(hasActiveFilter) {
            val optionList = combinePriceFilterIfExists(
                    element.activeFilterList.orEmpty(),
                    getString(R.string.tokopedianow_empty_product_filter_price_name)
            )
            val newOptionList = optionList.filter { !it.key.contains(EXCLUDE_PREFIX) }
            filterList.adapter = Adapter(newOptionList, tokoNowEmptyStateNoResultListener)
            filterList.layoutManager = layoutManager

            val chipSpacing = itemView.context.resources.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.unify_space_8
            )
            if (filterList.itemDecorationCount == 0)
                filterList.addItemDecoration(ChipSpacingItemDecoration(chipSpacing, chipSpacing))
        }
    }

    private fun bindGoToGlobalSearchButton(
        hasActiveFilter: Boolean,
        element: TokoNowEmptyStateNoResultUiModel
    ) {
        bindGlobalSearchBtnText(element)
        binding?.tokonowEmptyProductGlobalSearchButton?.showWithCondition(!hasActiveFilter)
        binding?.tokonowEmptyProductGlobalSearchButton?.setOnClickListener {
            tokoNowEmptyStateNoResultListener?.onFindInTokopediaClick()
        }
    }

    private fun bindGlobalSearchBtnText(element: TokoNowEmptyStateNoResultUiModel) {
        element.globalSearchBtnTextResId?.let {
            binding?.tokonowEmptyProductGlobalSearchButton?.text = getString(it)
        }
    }

    private fun bindChangeKeywordButton(hasActiveFilter: Boolean) {
        binding?.tokonowEmptyProductExploreTokopediaNow?.showWithCondition(!hasActiveFilter)
        binding?.tokonowEmptyProductExploreTokopediaNow?.setOnClickListener {
            tokoNowEmptyStateNoResultListener?.goToTokopediaNowHome()
        }
    }

    private class Adapter(
            private val optionList: List<Option>,
            private val tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener?,
    ): RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater
                    .from(parent.context)
                    .inflate(ViewHolder.LAYOUT, parent, false)

            return ViewHolder(view, tokoNowEmptyStateNoResultListener)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (position !in optionList.indices) return

            holder.bind(optionList[position])
        }

        override fun getItemCount() = optionList.size
    }

    private class ViewHolder(
            itemView: View,
            private val tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener?,
    ): RecyclerView.ViewHolder(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_tokopedianow_search_category_empty_state_chip
        }

        private var binding: ItemTokopedianowSearchCategoryEmptyStateChipBinding? by viewBinding()

        fun bind(option: Option) {
            binding?.tokonowSearchCategoryEmptyStateFilterChip?.apply {
                chipText = option.name
                chipType = ChipsUnify.TYPE_SELECTED
                chipSize = ChipsUnify.SIZE_SMALL
                setOnClickListener {
                    tokoNowEmptyStateNoResultListener?.onRemoveFilterClick(option)
                }
            }
        }
    }

    private class ChipSpacingItemDecoration(
            private val horizontalSpacing: Int,
            private val verticalSpacing: Int
    ): RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.right = this.horizontalSpacing
            outRect.bottom = this.verticalSpacing
        }
    }

    interface TokoNowEmptyStateNoResultListener {

        fun onFindInTokopediaClick()

        fun goToTokopediaNowHome()

        fun onRemoveFilterClick(option: Option)
    }
}
