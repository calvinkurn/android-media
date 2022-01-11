package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class SectionViewHolder (itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    lateinit var viewModel: SectionViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as SectionViewModel
        getSubComponent().inject(viewModel)
        Log.e("Section","BindView")
    }
}