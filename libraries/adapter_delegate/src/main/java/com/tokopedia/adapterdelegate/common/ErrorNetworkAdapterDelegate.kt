package com.tokopedia.adapterdelegate.common

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate

/**
 * Created by jegul on 2019-10-10
 */
class ErrorNetworkAdapterDelegate : TypedAdapterDelegate<ErrorNetworkModel, Any, ErrorNetworkViewHolder>(ErrorNetworkViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: ErrorNetworkModel, holder: ErrorNetworkViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ErrorNetworkViewHolder {
        return ErrorNetworkViewHolder(basicView)
    }
}