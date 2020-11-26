package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class EmptyStateViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var emptyStateViewModel: EmptyStateViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        emptyStateViewModel = discoveryBaseViewModel as EmptyStateViewModel
    }
}