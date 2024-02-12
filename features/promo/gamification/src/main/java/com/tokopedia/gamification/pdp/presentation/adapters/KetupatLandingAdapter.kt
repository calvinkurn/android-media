package com.tokopedia.gamification.pdp.presentation.adapters

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class KetupatLandingAdapter(val adapterFactory: KetupatLandingAdapterTypeFactory) :
    BaseAdapter<KetupatLandingAdapterTypeFactory>(adapterFactory) {

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        (holder.itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.isFullSpan = true
        super.onBindViewHolder(holder, position)
    }
}
