package com.tokopedia.discovery2.viewcontrollers.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

abstract class AbstractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel)

    open fun getInnerRecycleView(): RecyclerView? {
        return null
    }

    open fun onViewAttachedToWindow() {}

    open fun onViewDetachedToWindow() {}

    open fun onViewRemoving() {}
}