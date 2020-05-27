package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
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
            setClick(itemData?.applinks)
            promoText.setTextAndCheckShow(itemData?.description)
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