package com.tokopedia.filter.newdynamicfilter.adapter.typefactory

import android.view.View

import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterViewHolder

interface DynamicFilterTypeFactory {
    fun type(filter: Filter): Int

    fun createViewHolder(view: View, viewType: Int): DynamicFilterViewHolder
}
