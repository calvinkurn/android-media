package com.tokopedia.discovery2.viewcontrollers.adapter.viewholder

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

abstract class AbstractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindView(lifecycleOwner: LifecycleOwner, discoveryBaseViewModel: DiscoveryBaseViewModel)

    open fun onViewAttachedToWindow() {}

    open fun onViewDetachedToWindow() {}
}