package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.unifycomponents.UnifyButton

class FiltersEmptyStateViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var filtersEmptyStateViewModel: FiltersEmptyStateViewModel
    private val button: UnifyButton = itemView.findViewById(R.id.button)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        filtersEmptyStateViewModel = discoveryBaseViewModel as FiltersEmptyStateViewModel
        init()
    }

    private fun init() {
        button.setOnClickListener {
            filtersEmptyStateViewModel.resetChildComponents()
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            filtersEmptyStateViewModel.needReSyncLiveData.observe(lifecycle, {
                if (it) (fragment as DiscoveryFragment).reSync()
            })
        }
    }
}