package com.tokopedia.filter.bottomsheet.keywordfilter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.R
import com.tokopedia.filter.common.helper.ChipSpacingItemDecoration
import com.tokopedia.filter.common.helper.addItemDecorationIfNotExists
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifyprinciples.Typography

internal class KeywordFilterViewHolder(
    itemView: View,
    private val listener: KeywordFilterListener,
): AbstractViewHolder<KeywordFilterDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.sort_filter_bottom_sheet_keyword_filter_view_holder
    }

    private val layoutManager = ChipsLayoutManager
        .newBuilder(itemView.context)
        .setOrientation(ChipsLayoutManager.HORIZONTAL)
        .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
        .build()

    private val spacingItemDecoration = ChipSpacingItemDecoration(
        itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
        itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
    )

    private val keywordFilterTitle by lazy {
        itemView.findViewById<Typography?>(R.id.keywordFilterTitle)
    }

    private val keywordFilterRecyclerView by lazy {
        itemView.findViewById<RecyclerView?>(R.id.keywordFilterRecyclerView)
    }

    init {
        keywordFilterRecyclerView?.let {
            it.layoutManager = layoutManager
            it.isNestedScrollingEnabled = false
            it.addItemDecorationIfNotExists(spacingItemDecoration)
        }
    }

    override fun bind(element: KeywordFilterDataView) {
        keywordFilterTitle?.text = element.filter.title

        keywordFilterRecyclerView?.swapAdapter(
            KeywordFilterItemAdapter(element.itemList, listener),
            false,
        )
    }

    private class KeywordFilterItemAdapter(
        val keywordFilterItemList: List<KeywordFilterItemDataView>,
        val listener: KeywordFilterListener
    ): RecyclerView.Adapter<KeywordFilterItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordFilterItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sort_filter_bottom_sheet_chips_layout, parent, false)
            return KeywordFilterItemViewHolder(view, listener)
        }

        override fun getItemCount() = keywordFilterItemList.size

        override fun onBindViewHolder(holder: KeywordFilterItemViewHolder, position: Int) {
            holder.bind(keywordFilterItemList[position])
        }
    }

    private class KeywordFilterItemViewHolder(
        itemView: View,
        private val listener: KeywordFilterListener
    ): RecyclerView.ViewHolder(itemView) {

        private val sortFilterChipsUnify by lazy {
            itemView.findViewById<ChipsUnify?>(R.id.sortFilterChipsUnify)
        }

        fun bind(keywordFilterItemDataView: KeywordFilterItemDataView) {
            sortFilterChipsUnify?.apply {
                chipText = keywordFilterItemDataView.option.name
                chipSize = ChipsUnify.SIZE_MEDIUM
                chipType = ChipsUnify.TYPE_NORMAL
                setOnClickListener {
//                    listener.onSortItemClick(sortItemViewModel)
                }
                setOnRemoveListener {

                }
            }
        }
    }
}