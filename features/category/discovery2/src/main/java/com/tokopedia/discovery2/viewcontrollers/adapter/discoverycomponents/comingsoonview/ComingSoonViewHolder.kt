package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.comingsoonview

import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class ComingSoonViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private var comingSoonViewModel: ComingSoonViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        comingSoonViewModel = discoveryBaseViewModel as ComingSoonViewModel
    }
}
