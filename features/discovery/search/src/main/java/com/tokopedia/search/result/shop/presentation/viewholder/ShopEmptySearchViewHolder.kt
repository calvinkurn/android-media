package com.tokopedia.search.result.shop.presentation.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultShopEmptyLayoutBinding
import com.tokopedia.search.databinding.SearchResultShopEmptySelectedFilterItemBinding
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.SpacingItemDecoration
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.shop.presentation.model.ShopEmptySearchDataView
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*

internal class ShopEmptySearchViewHolder(
        itemView: View,
        private val emptyStateListener: EmptyStateListener
): AbstractViewHolder<ShopEmptySearchDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_result_shop_empty_layout
    }
    private var binding: SearchResultShopEmptyLayoutBinding? by viewBinding()

    private val context: Context = itemView.context

    override fun bind(element: ShopEmptySearchDataView?) {
        if (element == null) return

        bindTextViewContent(element)
        bindRecyclerViewSelectedFilter(element)
        bindButtonSearchAgain(element)
    }

    private fun bindTextViewContent(element: ShopEmptySearchDataView) {
        val contentText = getContentText(element)

        binding?.textViewSearchShopEmptyContent?.shouldShowWithAction(contentText.isNotEmpty()) {
            binding?.textViewSearchShopEmptyContent?.text = contentText
        }
    }

    private fun getContentText(element: ShopEmptySearchDataView): String {
        return if (element.isFilterActive) {
            String.format(context.getString(R.string.msg_empty_search_shop_content_with_filter), element.query)
        } else {
            String.format(context.getString(R.string.msg_empty_search_shop_content), element.query)
        }
    }

    private fun bindRecyclerViewSelectedFilter(element: ShopEmptySearchDataView) {
        binding?.recyclerViewSearchShopEmptySelectedFilter?.shouldShowWithAction(element.isFilterActive) {
            val selectedFilterAdapter = SelectedFilterAdapter(emptyStateListener)

            initRecyclerViewSelectedFilter(selectedFilterAdapter)
            populateSelectedFilterToRecylerView(selectedFilterAdapter)
        }
    }

    private fun initRecyclerViewSelectedFilter(selectedFilterAdapter: SelectedFilterAdapter) {
        binding?.recyclerViewSearchShopEmptySelectedFilter?.layoutManager = createSelectedFilterRecyclerViewLayoutManager()
        binding?.recyclerViewSearchShopEmptySelectedFilter?.adapter = selectedFilterAdapter

        if (binding?.recyclerViewSearchShopEmptySelectedFilter?.itemDecorationCount == 0) {
            binding?.recyclerViewSearchShopEmptySelectedFilter?.addItemDecoration(createSelectedFilterRecyclerViewItemDecoration())
        }

        binding?.recyclerViewSearchShopEmptySelectedFilter?.let {
            ViewCompat.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LTR)
        }
    }

    private fun createSelectedFilterRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return ChipsLayoutManager.newBuilder(context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
    }

    private fun createSelectedFilterRecyclerViewItemDecoration(): RecyclerView.ItemDecoration {
        return SpacingItemDecoration(context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8))
    }

    private fun populateSelectedFilterToRecylerView(selectedFilterAdapter: SelectedFilterAdapter) {
        val selectedFilterFromEmptyStateListener = emptyStateListener.getSelectedFilterAsOptionList()

        if (selectedFilterFromEmptyStateListener?.isNotEmpty() == true) {
            selectedFilterAdapter.setOptionList(selectedFilterFromEmptyStateListener)
        }
    }

    private fun bindButtonSearchAgain(element: ShopEmptySearchDataView) {
        binding?.buttonSearchShopEmptySearchAgain?.shouldShowWithAction(!element.isFilterActive) {
            binding?.buttonSearchShopEmptySearchAgain?.setOnClickListener {
                emptyStateListener.onEmptyButtonClicked()
            }
        }
    }

    private class SelectedFilterAdapter(private val clickListener: EmptyStateListener) : RecyclerView.Adapter<SelectedFilterItemViewHolder>() {
        private val optionList = ArrayList<Option>()

        fun setOptionList(optionList: List<Option>) {
            this.optionList.clear()
            this.optionList.addAll(optionList)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedFilterItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.search_result_shop_empty_selected_filter_item, parent, false)
            return SelectedFilterItemViewHolder(view, clickListener)
        }

        override fun onBindViewHolder(holder: SelectedFilterItemViewHolder, position: Int) {
            holder.bind(optionList[position])
        }

        override fun getItemCount(): Int {
            return optionList.size
        }
    }

    private class SelectedFilterItemViewHolder(
        itemView: View,
        private val clickListener: EmptyStateListener
    ) : RecyclerView.ViewHolder(itemView) {
        private var binding: SearchResultShopEmptySelectedFilterItemBinding? by viewBinding()

        fun bind(option: Option) {
            val binding = binding ?: return
            binding.filterText.text = option.name
            binding.filterText.setOnClickListener { clickListener.onSelectedFilterRemoved(option.uniqueId) }
        }
    }
}