package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.sliderbanner

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class SliderBannerItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val promoImage: ImageView = itemView.findViewById(R.id.banner_image)
    private lateinit var sliderBannerItemViewModel: SliderBannerItemViewModel


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        sliderBannerItemViewModel = discoveryBaseViewModel as SliderBannerItemViewModel
        sliderBannerItemViewModel.getComponentLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            val itemData = item.data?.get(0)
            ImageHandler.LoadImage(promoImage, itemData?.imageUrlMobile)
            setClick(itemData?.applinks)
        })

    }

    private fun setClick(appLinks: String?) {
        if (!appLinks.isNullOrEmpty()) {
            itemView.setOnClickListener {
                RouteManager.route(itemView.context, appLinks)
            }
        }
    }

}