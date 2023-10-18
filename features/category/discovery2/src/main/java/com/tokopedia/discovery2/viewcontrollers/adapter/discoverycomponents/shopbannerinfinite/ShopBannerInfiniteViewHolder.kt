package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class ShopBannerInfiniteViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var mShopBannerInfiniteViewModel: ShopBannerInfiniteViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mShopBannerInfiniteViewModel = discoveryBaseViewModel as ShopBannerInfiniteViewModel
        mShopBannerInfiniteViewModel?.checkForDarkMode(itemView.context)
        mShopBannerInfiniteViewModel?.let {
            getSubComponent().inject(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            mShopBannerInfiniteViewModel?.getSyncPageLiveData()?.observe(it) { needResync ->
                if (needResync) {
                    (fragment as DiscoveryFragment).reSync()
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mShopBannerInfiniteViewModel?.getSyncPageLiveData()?.removeObservers(it)
        }
    }
}
