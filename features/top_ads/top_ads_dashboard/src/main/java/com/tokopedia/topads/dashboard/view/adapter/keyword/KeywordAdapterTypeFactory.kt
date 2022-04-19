package com.tokopedia.topads.dashboard.view.adapter.keyword

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordItemModel


interface KeywordAdapterTypeFactory {

    fun type(model: KeywordItemModel): Int

    fun type(model: KeywordEmptyModel): Int

    fun holder(type: Int, view: View): KeywordViewHolder<*>

}