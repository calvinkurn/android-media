package com.tokopedia.search.result.shop.presentation.viewholder

import android.content.Context
import android.graphics.Typeface
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.LinearHorizontalSpacingDecoration
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.shop.presentation.model.ShopEmptySearchViewModel
import kotlinx.android.synthetic.main.search_result_shop_empty.view.*
import java.util.*

internal class ShopEmptySearchViewHolder(
        itemView: View,
        private val emptyStateListener: EmptyStateListener
): AbstractViewHolder<ShopEmptySearchViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_result_shop_empty
    }

    private val context: Context = itemView.context

    override fun bind(element: ShopEmptySearchViewModel?) {
        if (element == null) return

        bindTextViewContent(element)
        bindRecyclerViewSelectedFilter(element)
        bindButtonSearchAgain(element)
    }

    private fun bindTextViewContent(element: ShopEmptySearchViewModel) {
        val contentText = getContentText(element)

        itemView.textViewSearchShopEmptyContent?.shouldShowWithAction(contentText.isNotEmpty()) {
            itemView.textViewSearchShopEmptyContent?.text = boldTextBetweenQuotes(contentText)
        }
    }

    private fun getContentText(element: ShopEmptySearchViewModel): String {
        return if (element.isFilterActive) {
            String.format(context.getString(R.string.msg_empty_search_with_filter_2), element.query)
        } else {
            String.format(context.getString(R.string.empty_search_content_template), element.query)
        }
    }

    private fun boldTextBetweenQuotes(text: String): CharSequence {
        val quoteSymbol = "\""
        val firstQuotePos = text.indexOf(quoteSymbol)
        val lastQuotePos = text.lastIndexOf(quoteSymbol)

        if (firstQuotePos < 0) {
            return text
        }

        val str = SpannableStringBuilder(text)
        str.setSpan(StyleSpan(Typeface.BOLD), firstQuotePos, lastQuotePos + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return str
    }

    private fun bindRecyclerViewSelectedFilter(element: ShopEmptySearchViewModel) {
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
    }

    private fun createSelectedFilterRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun createSelectedFilterRecyclerViewItemDecoration(): RecyclerView.ItemDecoration {
        return LinearHorizontalSpacingDecoration(
                context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
                context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16)
        )
    }

    private fun populateSelectedFilterToRecylerView(selectedFilterAdapter: SelectedFilterAdapter) {
        val selectedFilterFromEmptyStateListener = emptyStateListener.selectedFilterAsOptionList

        if (selectedFilterFromEmptyStateListener?.isNotEmpty() == true) {
            selectedFilterAdapter.setOptionList(selectedFilterFromEmptyStateListener)
        }
    }

    private fun bindButtonSearchAgain(element: ShopEmptySearchViewModel) {
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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.search_filter_empty_state_selected_filter_item, parent, false)
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
        private val filterText: TextView = itemView.findViewById(R.id.filter_text)
        private val deleteButton: View = itemView.findViewById(R.id.delete_button)

        fun bind(option: Option) {
            filterText.text = option.name
            deleteButton.setOnClickListener { clickListener.onSelectedFilterRemoved(option.uniqueId) }
        }
    }
}