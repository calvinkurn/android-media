package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class BrandRecommendationItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var brandRecommendationItemViewModel: BrandRecommendationItemViewModel

    private val imageView: ImageView = itemView.findViewById(R.id.brand_recom_iv)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        brandRecommendationItemViewModel = discoveryBaseViewModel as BrandRecommendationItemViewModel
        brandRecommendationItemViewModel.ComponentData.observe(fragment.viewLifecycleOwner, Observer { item ->

            ImageHandler.LoadImage(imageView, item.data?.get(0)?.imageUrlMobile)
            setClick(item.data?.get(0)?.applinks)
        })

    }

    private fun setClick(applinks: String?) {
        if (!applinks.isNullOrEmpty()) {
            itemView.setOnClickListener {
                RouteManager.route(itemView.context, applinks)
            }
        }
    }


}