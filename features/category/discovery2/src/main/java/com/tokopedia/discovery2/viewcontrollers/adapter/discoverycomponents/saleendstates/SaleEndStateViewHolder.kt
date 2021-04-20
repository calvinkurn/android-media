package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.saleendstates

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class SaleEndStateViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var emptyStateViewModel: SaleEndStateViewModel
    private var saleStateCTa: TextView = itemView.findViewById(R.id.saleStateCTA)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        emptyStateViewModel = discoveryBaseViewModel as SaleEndStateViewModel
        saleStateCTa.setOnClickListener {
            (fragment as DiscoveryFragment).onRefresh()
        }
    }
}