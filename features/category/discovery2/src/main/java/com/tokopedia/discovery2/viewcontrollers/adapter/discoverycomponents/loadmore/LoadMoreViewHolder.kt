package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class LoadMoreViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var loadMoreViewModel: LoadMoreViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        loadMoreViewModel = discoveryBaseViewModel as LoadMoreViewModel
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            loadMoreViewModel.syncData.observe(lifecycleOwner, Observer {
                (fragment as DiscoveryFragment).reSync()
            })
        }

    }
}