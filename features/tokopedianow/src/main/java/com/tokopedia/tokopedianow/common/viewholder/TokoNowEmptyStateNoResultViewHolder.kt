package com.tokopedia.tokopedianow.common.viewholder

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper.EXCLUDE_PREFIX
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper.combinePriceFilterIfExists
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategoryEmptyProductBinding
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategoryEmptyStateChipBinding
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowEmptyStateNoResultViewHolder(
        itemView: View,
        private val tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultListener? = null,
        private val tokoNowEmptyStateNoResultTrackerListener: TokoNowEmptyStateNoResultTrackerListener? = null
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

        bindImage(element)
        bindTitle(hasActiveFilter, element)
        bindDescription(hasActiveFilter, element)
        bindFilterList(hasActiveFilter, element)
        bindPrimaryButton(hasActiveFilter, element)
        bindSecondaryButton(hasActiveFilter, element)
        impressRoot(element)
    }

    private fun bindImage(element: TokoNowEmptyStateNoResultUiModel) {
        if (element.defaultImage.isNotBlank()) {
            binding?.tokonowEmptyProductImage?.loadImage(element.defaultImage)
        }
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

            val newOptionList = if (optionList.any { it.key.startsWith(EXCLUDE_PREFIX) }) {
                optionList.filter {
                    it.key != SearchApiConst.SC && it != element.excludeFilter
                }
            } else {
                optionList
            }

            filterList.adapter = Adapter(newOptionList, tokoNowEmptyStateNoResultListener)
            filterList.layoutManager = layoutManager

            val chipSpacing = itemView.context.resources.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.unify_space_8
            )
            if (filterList.itemDecorationCount == 0)
                filterList.addItemDecoration(ChipSpacingItemDecoration(chipSpacing, chipSpacing))
        }
    }

    private fun bindPrimaryButton(
        hasActiveFilter: Boolean,
        element: TokoNowEmptyStateNoResultUiModel
    ) {
        binding?.apply {
            tokonowEmptyProductPrimaryButton.showIfWithBlock(
                predicate = (!hasActiveFilter || element.defaultTextPrimaryButton.isNotBlank()) && element.enablePrimaryButton
            ) {
                if (element.defaultTextPrimaryButton.isNotBlank()) {
                    text = element.defaultTextPrimaryButton
                    buttonType = UnifyButton.Type.MAIN
                    setOnClickListener {
                        tokoNowEmptyStateNoResultTrackerListener?.trackClickDefaultPrimaryButton()
                        RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${element.defaultUrlPrimaryButton}")
                    }
                } else {
                    element.globalSearchBtnTextResId?.let { text = getString(it) }
                    buttonType = UnifyButton.Type.ALTERNATE
                    setOnClickListener {
                        tokoNowEmptyStateNoResultListener?.onFindInTokopediaClick()
                    }
                }
            }
        }
    }

    private fun bindSecondaryButton(
        hasActiveFilter: Boolean,
        element: TokoNowEmptyStateNoResultUiModel
    ) {
        binding?.apply {
            tokonowEmptyProductSecondaryButton.showWithCondition(
                shouldShow = !hasActiveFilter && element.defaultTextPrimaryButton.isBlank() && element.enableSecondaryButton
            )
            tokonowEmptyProductSecondaryButton.setOnClickListener {
                tokoNowEmptyStateNoResultListener?.goToTokopediaNowHome()
            }
        }
    }

    private fun impressRoot(element: TokoNowEmptyStateNoResultUiModel) {
        binding?.root?.addOnImpressionListener(element) {
            tokoNowEmptyStateNoResultTrackerListener?.trackImpressEmptyStateNoResult()
        }
    }

    private class Adapter(
            private val optionList: List<Option>,
            private val tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultListener?,
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
            private val tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultListener?,
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

    interface TokoNowEmptyStateNoResultTrackerListener {
        fun trackImpressEmptyStateNoResult()

        fun trackClickDefaultPrimaryButton()
    }
}
