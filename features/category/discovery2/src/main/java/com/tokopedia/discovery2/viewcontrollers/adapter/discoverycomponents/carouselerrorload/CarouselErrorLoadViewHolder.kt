package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselerrorload

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.unifycomponents.ImageUnify

class CarouselErrorLoadViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var carouselErrorLoadViewModel: CarouselErrorLoadViewModel


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        carouselErrorLoadViewModel = discoveryBaseViewModel as CarouselErrorLoadViewModel
        with(itemView.context) {
            if(this is DiscoveryActivity) {
                this.discoveryComponent.provideSubComponent()
                        .inject(carouselErrorLoadViewModel)
            }
        }
        setLoaderView()
    }

    private fun setLoaderView() {
        itemView.findViewById<ImageUnify>(R.id.errorLoadMore).setOnClickListener{
            carouselErrorLoadViewModel.loadData()
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            carouselErrorLoadViewModel.syncData.observe(lifecycleOwner, Observer {
                (fragment as DiscoveryFragment).reSync()
            })
        }
    }
}