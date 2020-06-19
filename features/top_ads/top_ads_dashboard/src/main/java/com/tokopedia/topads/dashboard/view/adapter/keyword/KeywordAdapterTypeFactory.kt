package com.tokopedia.topads.dashboard.view.adapter.keyword

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordItemViewModel


interface KeywordAdapterTypeFactory {

    fun type(model: KeywordItemViewModel): Int

    fun type(model: KeywordEmptyViewModel): Int

    fun holder(type: Int, view: View): KeywordViewHolder<*>

}