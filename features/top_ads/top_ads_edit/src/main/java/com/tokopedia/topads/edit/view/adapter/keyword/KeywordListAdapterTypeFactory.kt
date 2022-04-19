package com.tokopedia.topads.edit.view.adapter.keyword

import android.view.View
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordItemViewModel

interface KeywordListAdapterTypeFactory {

    fun type(model: KeywordItemViewModel): Int

    fun holder(type: Int, view: View): KeywordViewHolder<*>

}