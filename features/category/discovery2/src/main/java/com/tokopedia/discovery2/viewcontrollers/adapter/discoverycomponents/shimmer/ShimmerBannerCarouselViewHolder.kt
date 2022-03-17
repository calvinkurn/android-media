package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer

import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class ShimmerBannerCarouselViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private lateinit var shimmerBannerCarouselViewModel: ShimmerViewModel
    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        shimmerBannerCarouselViewModel = discoveryBaseViewModel as ShimmerViewModel
    }
}