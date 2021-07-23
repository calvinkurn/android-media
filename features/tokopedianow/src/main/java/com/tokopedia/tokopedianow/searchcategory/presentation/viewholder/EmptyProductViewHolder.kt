package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper.combinePriceFilterIfExists
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.EmptyProductListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.EmptyProductDataView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class EmptyProductViewHolder(
        itemView: View,
        private val emptyProductListener: EmptyProductListener,
): AbstractViewHolder<EmptyProductDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_empty_product
    }

    private val titleTextView: Typography? = itemView.findViewById(
            R.id.tokonowEmptyProductTitle
    )
    private val descriptionTextView: Typography? = itemView.findViewById(
            R.id.tokonowEmptyProductDescription
    )
    private val filterList: RecyclerView? = itemView.findViewById(
            R.id.tokonowEmptyProductFilterList
    )
    private val globalSearchButton: UnifyButton? = itemView.findViewById(
            R.id.tokonowEmptyProductGlobalSearchButton
    )
    private val exploreTokopediaNowButton: UnifyButton? = itemView.findViewById(
            R.id.tokonowEmptyProductExploreTokopediaNow
    )

    private val layoutManager = ChipsLayoutManager
            .newBuilder(itemView.context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()

    override fun bind(element: EmptyProductDataView?) {
        element ?: return

        val hasActiveFilter = element.activeFilterList.isNotEmpty()

        bindTitle(hasActiveFilter)
        bindDescription(hasActiveFilter)
        bindFilterList(hasActiveFilter, element)
        bindGoToGlobalSearchButton(hasActiveFilter)
        bindChangeKeywordButton(hasActiveFilter)
    }

    private fun bindTitle(hasActiveFilter: Boolean) {
        val title = if (hasActiveFilter) getString(R.string.tokopedianow_empty_product_filter_title)
        else getString(R.string.tokopedianow_empty_product_title)

        titleTextView?.text = title
    }

    private fun bindDescription(hasActiveFilter: Boolean) {
        val description = if (hasActiveFilter) getString(R.string.tokopedianow_empty_product_filter_description)
        else getString(R.string.tokopedianow_empty_product_description)

        descriptionTextView?.text = description
    }

    private fun bindFilterList(hasActiveFilter: Boolean, element: EmptyProductDataView) {
        val filterList = filterList ?: return

        filterList.shouldShowWithAction(hasActiveFilter) {
            val optionList = combinePriceFilterIfExists(
                    element.activeFilterList,
                    getString(R.string.tokopedianow_empty_product_filter_price_name)
            )
            filterList.adapter = Adapter(optionList, emptyProductListener)
            filterList.layoutManager = layoutManager

            val chipSpacing = itemView.context.resources.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.unify_space_8
            )
            if (filterList.itemDecorationCount == 0)
                filterList.addItemDecoration(ChipSpacingItemDecoration(chipSpacing, chipSpacing))
        }
    }

    private fun bindGoToGlobalSearchButton(hasActiveFilter: Boolean) {
        globalSearchButton?.showWithCondition(!hasActiveFilter)
        globalSearchButton?.setOnClickListener {
            emptyProductListener.onFindInTokopediaClick()
        }
    }

    private fun bindChangeKeywordButton(hasActiveFilter: Boolean) {
        exploreTokopediaNowButton?.showWithCondition(!hasActiveFilter)
        exploreTokopediaNowButton?.setOnClickListener {
            emptyProductListener.goToTokopediaNowHome()
        }
    }

    private class Adapter(
            private val optionList: List<Option>,
            private val listener: EmptyProductListener,
    ): RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater
                    .from(parent.context)
                    .inflate(ViewHolder.LAYOUT, parent, false)

            return ViewHolder(view, listener)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (position !in optionList.indices) return

            holder.bind(optionList[position])
        }

        override fun getItemCount() = optionList.size
    }

    private class ViewHolder(
            itemView: View,
            private val emptyProductListener: EmptyProductListener,
    ): RecyclerView.ViewHolder(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_tokopedianow_search_category_empty_state_chip
        }

        private val chip: ChipsUnify? = itemView.findViewById(R.id.tokonowSearchCategoryEmptyStateFilterChip)

        fun bind(option: Option) {
            chip?.chipText = option.name
            chip?.chipType = ChipsUnify.TYPE_SELECTED
            chip?.chipSize = ChipsUnify.SIZE_SMALL
            chip?.setOnClickListener {
                emptyProductListener.onRemoveFilterClick(option)
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
}