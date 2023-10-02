package com.tokopedia.filter.bottomsheet.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.R
import com.tokopedia.filter.common.helper.ChipSpacingItemDecoration
import com.tokopedia.filter.common.helper.addItemDecorationIfNotExists
import com.tokopedia.filter.common.helper.createColorSampleDrawable
import com.tokopedia.filter.common.helper.expandTouchArea
import com.tokopedia.filter.databinding.SortFilterBottomSheetChipsLayoutBinding
import com.tokopedia.filter.databinding.SortFilterBottomSheetFilterViewHolderBinding
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import com.tokopedia.utils.view.binding.viewBinding

internal class FilterViewHolder(
    itemView: View,
    private val recycledViewPool: RecycledViewPool,
    private val filterViewListener: FilterViewListener
): AbstractViewHolder<FilterViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.sort_filter_bottom_sheet_filter_view_holder
    }
    private var binding : SortFilterBottomSheetFilterViewHolderBinding? by viewBinding()

    private val layoutManager = ChipsLayoutManager
            .newBuilder(itemView.context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()

    private val spacingItemDecoration = ChipSpacingItemDecoration(
            itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
            itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
    )

    init {
        binding?.optionRecyclerView?.let {
            it.setRecycledViewPool(recycledViewPool)
            it.layoutManager = layoutManager
            it.isNestedScrollingEnabled = false
            it.addItemDecorationIfNotExists(spacingItemDecoration)
        }
    }

    override fun bind(element: FilterViewModel) {
        bindTitle(element)
        bindOptionList(element)
        bindSeeAll(element)
    }

    private fun bindTitle(element: FilterViewModel) {
        binding?.filterTitleTextView?.let { TextAndContentDescriptionUtil.setTextAndContentDescription(it, element.filter.title, getString(R.string.content_desc_filterTitleTextView)) }
    }

    private fun bindOptionList(element: FilterViewModel) {
        binding?.optionRecyclerView?.swapAdapter(
            OptionAdapter(element, filterViewListener),
            false
        )
    }

    private fun bindSeeAll(element: FilterViewModel) {
        binding?.filterSeeAll?.showWithCondition(element.hasSeeAllButton)
        binding?.filterSeeAll?.post {
            binding?.filterSeeAll?.expandTouchArea(
                    0,
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_18),
                    0,
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_18)
            )
        }
        binding?.filterSeeAll?.setOnClickListener {
            filterViewListener.onSeeAllOptionClick(element)
        }
    }

    private class OptionAdapter(
        val filterViewModel: FilterViewModel,
        val filterViewListener: FilterViewListener
    ): RecyclerView.Adapter<OptionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sort_filter_bottom_sheet_chips_layout, parent, false)
            return OptionViewHolder(view, filterViewModel, filterViewListener)
        }

        override fun getItemCount() = filterViewModel.optionViewModelList.size

        override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
            holder.bind(filterViewModel.optionViewModelList[position])
        }
    }

    private class OptionViewHolder(
        itemView: View,
        private val filterViewModel: FilterViewModel,
        private val filterViewListener: FilterViewListener
    ): RecyclerView.ViewHolder(itemView) {
        private var binding: SortFilterBottomSheetChipsLayoutBinding? by viewBinding()

        fun bind(optionViewModel: OptionViewModel) {
            val sortFilterChipsUnify = binding?.sortFilterChipsUnify ?: return
            sortFilterChipsUnify.chipText = optionViewModel.option.name
            sortFilterChipsUnify.chipType =
                    if (optionViewModel.isSelected) ChipsUnify.TYPE_SELECTED
                    else ChipsUnify.TYPE_NORMAL
            sortFilterChipsUnify.chipSize = ChipsUnify.SIZE_MEDIUM
            sortFilterChipsUnify.showIcon(optionViewModel)
            sortFilterChipsUnify.showNewNotification = optionViewModel.option.isNew
            sortFilterChipsUnify.setOnClickListener {
                filterViewListener.onOptionClick(filterViewModel, optionViewModel)
            }
        }

        private fun ChipsUnify.showIcon(optionViewModel: OptionViewModel) {
            if (optionViewModel.option.isCategoryOption) return

            when {
                optionViewModel.option.hexColor.isNotEmpty() -> showColorSample(optionViewModel)
                optionViewModel.option.isRatingOption -> showIconRating()
                optionViewModel.option.iconUrl.isNotEmpty() -> showIconFromUrl(optionViewModel)
            }
        }

        private fun ChipsUnify.showColorSample(optionViewModel: OptionViewModel) {
            chip_image_icon.visibility = View.VISIBLE

            val colorSampleDrawable = createColorSampleDrawable(itemView.context, optionViewModel.option.hexColor)
            chip_image_icon.setImageDrawable(colorSampleDrawable)
        }

        private fun ChipsUnify.showIconRating() {
            chip_image_icon.visibility = View.VISIBLE
            chip_image_icon.setImageResource(R.drawable.ic_app_rating_filled)
        }

        private fun ChipsUnify.showIconFromUrl(optionViewModel: OptionViewModel) {
            chip_image_icon.visibility = View.VISIBLE
            chip_image_icon.setImageUrl(optionViewModel.option.iconUrl)
        }
    }
}
