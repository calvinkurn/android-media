package com.tokopedia.feedcomponent.helper.common

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.feedcomponent.helper.TypedAdapterDelegate

/**
 * Created by jegul on 2019-10-10
 */
class LoadingAdapterDelegate : TypedAdapterDelegate<LoadingModel, Any, LoadingViewholder>(LoadingViewholder.LAYOUT) {

    override fun onBindViewHolder(item: LoadingModel, holder: LoadingViewholder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): LoadingViewholder {
        return LoadingViewholder(basicView)
    }
}