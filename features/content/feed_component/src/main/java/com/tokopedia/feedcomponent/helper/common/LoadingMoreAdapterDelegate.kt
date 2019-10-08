package com.tokopedia.feedcomponent.helper.common

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingMoreViewHolder
import com.tokopedia.feedcomponent.helper.TypedAdapterDelegate

/**
 * Created by jegul on 2019-10-02.
 */
object LoadingMoreAdapterDelegate : TypedAdapterDelegate<LoadingMoreModel, Any, LoadingMoreViewHolder>(LoadingMoreViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: LoadingMoreModel, holder: LoadingMoreViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, view: View): LoadingMoreViewHolder {
        return LoadingMoreViewHolder(view)
    }
}