package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.AddChildAdapterCallback
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment


class ProductCardRevampViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView,fragment.viewLifecycleOwner) {

    private lateinit var mProductRevampComponentViewModel: ProductCardRevampViewModel
    private var addChildAdapterCallback: AddChildAdapterCallback = (fragment as AddChildAdapterCallback)


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductRevampComponentViewModel = discoveryBaseViewModel as ProductCardRevampViewModel
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            mProductRevampComponentViewModel.getProductCarouselItemsListData().observe(it, Observer { item ->
                addChildAdapterCallback.notifyMergeAdapter()
            })
            mProductRevampComponentViewModel.getSyncPageLiveData().observe(it, Observer { needResync ->
                if (needResync) {
                    (fragment as DiscoveryFragment).reSync()
                }

            })
        }

    }

}