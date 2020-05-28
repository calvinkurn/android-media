package com.tokopedia.discovery2.viewcontrollers.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

abstract class AbstractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected var parentAbstractViewHolder : AbstractViewHolder? = null

    // TODO Remove this implementation
    abstract fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel)

    fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel, parentViewHolder: AbstractViewHolder?) {
        this.parentAbstractViewHolder = parentViewHolder
        this.bindView(discoveryBaseViewModel)
    }

    open fun getInnerRecycleView(): RecyclerView? {
        return null
    }

    open fun onViewAttachedToWindow() {}

    open fun onViewDetachedToWindow() {}

    open fun onViewRemoving() {}
}