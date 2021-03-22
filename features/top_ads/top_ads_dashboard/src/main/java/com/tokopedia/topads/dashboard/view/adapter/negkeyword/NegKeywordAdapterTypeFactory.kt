package com.tokopedia.topads.dashboard.view.adapter.negkeyword

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewholder.NegKeywordViewHolder
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordItemModel


interface NegKeywordAdapterTypeFactory {

    fun type(model: NegKeywordItemModel): Int

    fun type(model: NegKeywordEmptyModel): Int

    fun holder(type: Int, view: View): NegKeywordViewHolder<*>

}