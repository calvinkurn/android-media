package com.tokopedia.topads.edit.view.adapter.keyword

import android.view.View
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordGroupViewHolder
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordItemViewHolder
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordGroupViewModel
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordItemViewModel

class KeywordListAdapterTypeFactoryImpl(var actionSelected: ((pos: Int) -> Unit)?) : KeywordListAdapterTypeFactory {
    override fun type(model: KeywordGroupViewModel): Int = KeywordGroupViewHolder.LAYOUT

    override fun type(model: KeywordItemViewModel): Int = KeywordItemViewHolder.LAYOUT


    override fun holder(type: Int, view: View): KeywordViewHolder<*> {
        return when (type) {
            KeywordItemViewHolder.LAYOUT -> KeywordItemViewHolder(view, actionSelected)
            KeywordGroupViewHolder.LAYOUT -> KeywordGroupViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }
}