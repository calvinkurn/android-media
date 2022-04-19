package com.tokopedia.topads.dashboard.view.adapter.keyword

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder.KeywordEmptyViewHolder
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder.KeywordItemViewHolder
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordItemModel

class KeywordAdapterTypeFactoryImpl(var onSwitchAction: ((pos: Int, isChecked: Boolean) -> Unit),
                                    var onSelectMode: ((select: Boolean) -> Unit),
                                    private val addKeywords: (() -> Unit),
                                    var isHeadline: Boolean = false) : KeywordAdapterTypeFactory {

    override fun type(model: KeywordItemModel) = KeywordItemViewHolder.LAYOUT

    override fun type(model: KeywordEmptyModel) = KeywordEmptyViewHolder.LAYOUT

    override fun holder(type: Int, view: View): KeywordViewHolder<*> {
        return when (type) {
            KeywordEmptyViewHolder.LAYOUT -> KeywordEmptyViewHolder(view, addKeywords)
            KeywordItemViewHolder.LAYOUT -> KeywordItemViewHolder(view, onSwitchAction, onSelectMode, isHeadline)
            else -> throw RuntimeException("Illegal view type")
        }
    }
}