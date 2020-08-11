package com.tokopedia.topads.dashboard.view.adapter.negkeyword

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewholder.NegKeywordEmptyViewHolder
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewholder.NegKeywordItemViewHolder
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewholder.NegKeywordViewHolder
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordItemViewModel

class NegKeywordAdapterTypeFactoryImpl(
        var onSelectMode: ((select: Boolean) -> Unit),
        private val addKeywords: (() -> Unit)) : NegKeywordAdapterTypeFactory {

    override fun type(model: NegKeywordItemViewModel) = NegKeywordItemViewHolder.LAYOUT

    override fun type(model: NegKeywordEmptyViewModel) = NegKeywordEmptyViewHolder.LAYOUT

    override fun holder(type: Int, view: View): NegKeywordViewHolder<*> {
        return when (type) {
            NegKeywordEmptyViewHolder.LAYOUT -> NegKeywordEmptyViewHolder(view,addKeywords)
            NegKeywordItemViewHolder.LAYOUT -> NegKeywordItemViewHolder(view, onSelectMode)
            else -> throw RuntimeException("Illegal view type")
        }
    }
}