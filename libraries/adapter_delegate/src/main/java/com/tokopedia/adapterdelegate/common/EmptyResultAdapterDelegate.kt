package com.tokopedia.adapterdelegate.common

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate

/**
 * Created by jegul on 2019-10-10
 */
class EmptyResultAdapterDelegate : TypedAdapterDelegate<EmptyResultViewModel, Any, EmptyResultViewHolder>(EmptyResultViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: EmptyResultViewModel, holder: EmptyResultViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): EmptyResultViewHolder {
        return EmptyResultViewHolder(basicView)
    }
}