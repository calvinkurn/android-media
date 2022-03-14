package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.show


class ShopBannerInfiniteViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var mShopBannerInfiniteViewModel: ShopBannerInfiniteViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mShopBannerInfiniteViewModel = discoveryBaseViewModel as ShopBannerInfiniteViewModel
        getSubComponent().inject(mShopBannerInfiniteViewModel)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            mShopBannerInfiniteViewModel.getSyncPageLiveData().observe(it, { needResync ->
                if (needResync) {
                    (fragment as DiscoveryFragment).reSync()
                }
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mShopBannerInfiniteViewModel.getSyncPageLiveData().removeObservers(it)
        }
    }
}