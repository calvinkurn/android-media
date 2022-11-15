package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment


class ContentCardViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var mContentCardViewModel: ContentCardViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mContentCardViewModel = discoveryBaseViewModel as ContentCardViewModel
        mContentCardViewModel.checkForDarkMode(itemView.context)
        getSubComponent().inject(mContentCardViewModel)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            mContentCardViewModel.getSyncPageLiveData().observe(it) { needResync ->
                if (needResync) {
                    (fragment as DiscoveryFragment).reSync()
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mContentCardViewModel.getSyncPageLiveData().removeObservers(it)
        }
    }
}
