package com.tokopedia.topads.view.adapter.keyword

import android.view.View
import com.tokopedia.topads.view.adapter.keyword.viewholder.KeywordEmptyViewHolder
import com.tokopedia.topads.view.adapter.keyword.viewholder.KeywordGroupViewHolder
import com.tokopedia.topads.view.adapter.keyword.viewholder.KeywordItemViewHolder
import com.tokopedia.topads.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordEmptyViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordGroupViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordItemViewModel

class KeywordListAdapterTypeFactoryImpl(var actionSelected: (() -> Unit)?) : KeywordListAdapterTypeFactory {
    override fun type(model: KeywordGroupViewModel): Int = KeywordGroupViewHolder.LAYOUT

    override fun type(model: KeywordItemViewModel): Int = KeywordItemViewHolder.LAYOUT

    override fun type(model: KeywordEmptyViewModel): Int = KeywordEmptyViewHolder.LAYOUT

    override fun holder(type: Int, view: View): KeywordViewHolder<*> {
        return when(type){
            KeywordItemViewHolder.LAYOUT -> KeywordItemViewHolder(view, actionSelected)
            KeywordGroupViewHolder.LAYOUT -> KeywordGroupViewHolder(view)
            KeywordEmptyViewHolder.LAYOUT -> KeywordEmptyViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }
}