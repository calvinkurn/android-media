package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannedview

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.DefaultComponentViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.unifyprinciples.Typography

class BannedViewViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private lateinit var bannedViewModel: DefaultComponentViewModel
    private val header: Typography = itemView.findViewById(R.id.txt_header)
    private val subHeader: Typography = itemView.findViewById(R.id.txt_sub_header)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        bannedViewModel = discoveryBaseViewModel as DefaultComponentViewModel
        bannedViewModel.getComponentLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            header.text = item.title
            subHeader.text = item.description
        })
    }
}