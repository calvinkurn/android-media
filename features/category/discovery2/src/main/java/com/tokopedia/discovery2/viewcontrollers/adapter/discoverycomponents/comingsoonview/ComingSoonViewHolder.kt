package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.comingsoonview

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class ComingSoonViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {


    private lateinit var comingSoonViewModel: ComingSoonViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        comingSoonViewModel = discoveryBaseViewModel as ComingSoonViewModel
    }
}