package com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageBannerEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageBannerMapper
import com.tokopedia.salam.umrah.homepage.presentation.fragment.UmrahHomepageFragment
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import kotlinx.android.synthetic.main.partial_umrah_home_page_banner.view.*

class UmrahHomepageBannerViewHolder(view: View, private val onBindListener: onItemBindListener) : AbstractViewHolder<UmrahHomepageBannerEntity>(view){
    override fun bind(element: UmrahHomepageBannerEntity) {
        with(itemView) {
            if (element.isLoaded && element.data.isNotEmpty()) {

                umrah_banner_shimmering.hide()
                banner_umrah_home_page.apply {
                    show()
                    customWidth = Resources.getSystem().displayMetrics.widthPixels-120
                    setBannerIndicator(GREEN_INDICATOR)
                    val listImageUrl = UmrahHomepageBannerMapper.bannerMappertoString(element.data)
                    setPromoList(listImageUrl)
                    setOnPromoClickListener {
                        onBindListener.onClickBanner(element.data[it], it)
                        RouteManager.route(context, element.data[it].applinkUrl)
                    }
                    setOnPromoScrolledListener {
                        onBindListener.onImpressionBanner(element.data[it],it)
                    }
                    buildView()

                }


            } else {
                umrah_banner_shimmering.show()
                banner_umrah_home_page.hide()
                if (!UmrahHomepageFragment.isRequestedBanner) {
                    onBindListener.onBindBannerVH(element.isLoadFromCloud)
                    UmrahHomepageFragment.isRequestedBanner = true
                }  else{

                }
            }
        }
    }

    companion object{
        val LAYOUT = R.layout.partial_umrah_home_page_banner
        val GREEN_INDICATOR = 1
    }
}