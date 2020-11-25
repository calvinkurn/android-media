package com.tokopedia.topads.view.adapter.keyword

import android.view.View
import com.tokopedia.topads.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordItemViewModel

/**
 * Author errysuprayogi on 11,November,2019
 */
interface KeywordListAdapterTypeFactory {

    fun type(model: KeywordItemViewModel): Int

    fun holder(type: Int, view: View): KeywordViewHolder<*>

}