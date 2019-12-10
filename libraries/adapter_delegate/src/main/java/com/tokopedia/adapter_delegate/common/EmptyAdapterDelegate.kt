package com.tokopedia.adapter_delegate.common

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.adapter_delegate.TypedAdapterDelegate

/**
 * Created by jegul on 2019-10-10
 */
class EmptyAdapterDelegate : TypedAdapterDelegate<EmptyModel, Any, EmptyViewHolder>(EmptyViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: EmptyModel, holder: EmptyViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): EmptyViewHolder {
        return EmptyViewHolder(basicView)
    }
}