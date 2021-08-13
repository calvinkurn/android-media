package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner

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
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifyprinciples.Typography

class CarouselBannerItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val promoImage: ImageView = itemView.findViewById(R.id.promo_image)
    private val promoText: Typography = itemView.findViewById(R.id.promo_text)
    private lateinit var carouselBannerItemViewModel: CarouselBannerItemViewModel


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        carouselBannerItemViewModel = discoveryBaseViewModel as CarouselBannerItemViewModel
        carouselBannerItemViewModel.getComponentLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            val itemData = item.data?.get(0)
            promoImage.loadImageWithoutPlaceholder(itemData?.imageUrlMobile ?: "")
            setClick(itemData)
            promoText.setTextAndCheckShow(itemData?.description)
        })

    }

    private fun setClick(dataItem: DataItem?) {
        dataItem?.let {
            if (!it.applinks.isNullOrEmpty()) {
                itemView.setOnClickListener { itemView ->
                    RouteManager.route(itemView.context, it.applinks)
                    (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackBannerClick(it, adapterPosition)
                }
            }
        }
    }

}