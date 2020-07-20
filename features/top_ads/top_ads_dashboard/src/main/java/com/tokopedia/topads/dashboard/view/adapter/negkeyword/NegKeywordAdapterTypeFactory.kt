package com.tokopedia.topads.dashboard.view.adapter.negkeyword

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewholder.NegKeywordViewHolder
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordItemViewModel


interface NegKeywordAdapterTypeFactory {

    fun type(model: NegKeywordItemViewModel): Int

    fun type(model: NegKeywordEmptyViewModel): Int

    fun holder(type: Int, view: View): NegKeywordViewHolder<*>

}