package com.tokopedia.discovery2.viewcontrollers.adapter.viewholder

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

abstract class AbstractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindView(fragment: Fragment, discoveryBaseViewModel: DiscoveryBaseViewModel)
}