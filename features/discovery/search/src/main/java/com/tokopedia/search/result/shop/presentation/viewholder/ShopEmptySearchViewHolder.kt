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
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.SpacingItemDecoration
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.shop.presentation.model.ShopEmptySearchDataView
import kotlinx.android.synthetic.main.search_result_shop_empty_layout.view.*
import kotlinx.android.synthetic.main.search_result_shop_empty_selected_filter_item.view.*
import java.util.*

internal class ShopEmptySearchViewHolder(
        itemView: View,
        private val emptyStateListener: EmptyStateListener
): AbstractViewHolder<ShopEmptySearchDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_result_shop_empty_layout
    }

    private val context: Context = itemView.context

    override fun bind(element: ShopEmptySearchDataView?) {
        if (element == null) return

        bindTextViewContent(element)
        bindRecyclerViewSelectedFilter(element)
        bindButtonSearchAgain(element)
    }

    private fun bindTextViewContent(element: ShopEmptySearchDataView) {
        val contentText = getContentText(element)

        itemView.textViewSearchShopEmptyContent?.shouldShowWithAction(contentText.isNotEmpty()) {
            itemView.textViewSearchShopEmptyContent?.text = contentText
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
        itemView.recyclerViewSearchShopEmptySelectedFilter?.shouldShowWithAction(element.isFilterActive) {
            val selectedFilterAdapter = SelectedFilterAdapter(emptyStateListener)

            initRecyclerViewSelectedFilter(selectedFilterAdapter)
            populateSelectedFilterToRecylerView(selectedFilterAdapter)
        }
    }

    private fun initRecyclerViewSelectedFilter(selectedFilterAdapter: SelectedFilterAdapter) {
        itemView.recyclerViewSearchShopEmptySelectedFilter?.layoutManager = createSelectedFilterRecyclerViewLayoutManager()
        itemView.recyclerViewSearchShopEmptySelectedFilter?.adapter = selectedFilterAdapter

        if (itemView.recyclerViewSearchShopEmptySelectedFilter?.itemDecorationCount == 0) {
            itemView.recyclerViewSearchShopEmptySelectedFilter?.addItemDecoration(createSelectedFilterRecyclerViewItemDecoration())
        }

        itemView.recyclerViewSearchShopEmptySelectedFilter?.let {
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
        val selectedFilterFromEmptyStateListener = emptyStateListener.selectedFilterAsOptionList

        if (selectedFilterFromEmptyStateListener?.isNotEmpty() == true) {
            selectedFilterAdapter.setOptionList(selectedFilterFromEmptyStateListener)
        }
    }

    private fun bindButtonSearchAgain(element: ShopEmptySearchDataView) {
        itemView.buttonSearchShopEmptySearchAgain?.shouldShowWithAction(!element.isFilterActive) {
            itemView.buttonSearchShopEmptySearchAgain?.setOnClickListener {
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

    private class SelectedFilterItemViewHolder(itemView: View, private val clickListener: EmptyStateListener) : RecyclerView.ViewHolder(itemView) {

        fun bind(option: Option) {
            itemView.filterText?.text = option.name
            itemView.filterText?.setOnClickListener { clickListener.onSelectedFilterRemoved(option.uniqueId) }
        }
    }
}