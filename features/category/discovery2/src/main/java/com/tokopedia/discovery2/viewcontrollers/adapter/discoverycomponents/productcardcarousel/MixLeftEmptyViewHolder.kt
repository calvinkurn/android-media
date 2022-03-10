package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.data.MixLeft
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class MixLeftEmptyViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var mixLeftData: MixLeft? = null
    private lateinit var viewModel: MixLeftEmptyViewModel

    init {
        itemView.setOnClickListener {
            mixLeftData?.let {
                if (!it.applink.isNullOrEmpty()) {
                    (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackMixLeftBannerClick(viewModel.components)
                    RouteManager.route(fragment.context, it.applink)
                }
            }
        }
    }
    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as MixLeftEmptyViewModel
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().sendMixLeftBannerImpression(viewModel.components)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            viewModel.getMixLeftBannerDataLD().observe(lifecycle, { data ->
                mixLeftData = data
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            viewModel.getMixLeftBannerDataLD().removeObservers(lifecycle)
        }
    }
}