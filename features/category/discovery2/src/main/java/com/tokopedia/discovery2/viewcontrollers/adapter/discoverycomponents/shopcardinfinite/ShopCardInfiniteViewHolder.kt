package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcardinfinite

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment


class ShopCardInfiniteViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var mShopCardInfiniteViewModel: ShopCardInfiniteViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mShopCardInfiniteViewModel = discoveryBaseViewModel as ShopCardInfiniteViewModel
        getSubComponent().inject(mShopCardInfiniteViewModel)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            mShopCardInfiniteViewModel.getSyncPageLiveData().observe(it, { needResync ->
                if (needResync) {
                    (fragment as DiscoveryFragment).reSync()
                }
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mShopCardInfiniteViewModel.getSyncPageLiveData().removeObservers(it)
        }
    }
}