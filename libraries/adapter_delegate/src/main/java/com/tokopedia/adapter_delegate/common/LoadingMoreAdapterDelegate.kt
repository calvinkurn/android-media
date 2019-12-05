package com.tokopedia.adapter_delegate.common

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingMoreViewHolder
import com.tokopedia.adapter_delegate.TypedAdapterDelegate

/**
 * Created by jegul on 2019-10-02.
 */
class LoadingMoreAdapterDelegate : TypedAdapterDelegate<LoadingMoreModel, Any, LoadingMoreViewHolder>(LoadingMoreViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: LoadingMoreModel, holder: LoadingMoreViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): LoadingMoreViewHolder {
        return LoadingMoreViewHolder(basicView)
    }
}