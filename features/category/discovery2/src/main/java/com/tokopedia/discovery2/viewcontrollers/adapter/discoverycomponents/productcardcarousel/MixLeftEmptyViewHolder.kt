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
    private var viewModel: MixLeftEmptyViewModel? = null

    init {
        itemView.setOnClickListener {
            mixLeftData?.let {
                if (!it.applink.isNullOrEmpty()) {
                    viewModel?.components?.let { it1 -> (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackMixLeftBannerClick(it1) }
                    RouteManager.route(fragment.context, it.applink)
                }
            }
        }
    }
    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as MixLeftEmptyViewModel
        viewModel?.components?.let { (fragment as DiscoveryFragment).getDiscoveryAnalytics().sendMixLeftBannerImpression(it) }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            viewModel?.getMixLeftBannerDataLD()?.observe(lifecycle) { data ->
                mixLeftData = data
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            viewModel?.getMixLeftBannerDataLD()?.removeObservers(lifecycle)
        }
    }
}
