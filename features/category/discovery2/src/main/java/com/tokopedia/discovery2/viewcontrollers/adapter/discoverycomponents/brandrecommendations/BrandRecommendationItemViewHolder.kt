package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder

class BrandRecommendationItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var brandRecommendationItemViewModel: BrandRecommendationItemViewModel

    private val imageView: ImageView = itemView.findViewById(R.id.brand_recom_iv)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        brandRecommendationItemViewModel = discoveryBaseViewModel as BrandRecommendationItemViewModel
        brandRecommendationItemViewModel.getComponentDataLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            item.data?.get(0)?.let {
                it.imageUrlMobile?.let { it1 -> imageView.loadImageWithoutPlaceholder(it1) }
                setClick(item.data?.get(0))
            }
        })
    }

    private fun setClick(data: DataItem?) {
        data?.let {
            if (!it.applinks.isNullOrEmpty()) {
                itemView.setOnClickListener { itemView ->
                    RouteManager.route(itemView.context, it.applinks)
                    sendClickBrandRecommendationClickEvent(it)
                }
            }
        }
    }

    private fun sendClickBrandRecommendationClickEvent(it: DataItem) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackBannerClick(it, adapterPosition)
    }
}