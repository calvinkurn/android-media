package com.tokopedia.topads.dashboard.view.adapter.keyword

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder.KeywordEmptyViewHolder
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder.KeywordItemViewHolder
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordItemViewModel

class KeywordAdapterTypeFactoryImpl(var onSwitchAction: ((pos: Int, isChecked: Boolean) -> Unit),
                                    var onSelectMode: ((select: Boolean) -> Unit),
                                    private val addKeywords: (() -> Unit)) : KeywordAdapterTypeFactory {

    override fun type(model: KeywordItemViewModel) = KeywordItemViewHolder.LAYOUT

    override fun type(model: KeywordEmptyViewModel) = KeywordEmptyViewHolder.LAYOUT

    override fun holder(type: Int, view: View): KeywordViewHolder<*> {
        return when (type) {
            KeywordEmptyViewHolder.LAYOUT -> KeywordEmptyViewHolder(view, addKeywords)
            KeywordItemViewHolder.LAYOUT -> KeywordItemViewHolder(view, onSwitchAction, onSelectMode)
            else -> throw RuntimeException("Illegal view type")
        }
    }
}