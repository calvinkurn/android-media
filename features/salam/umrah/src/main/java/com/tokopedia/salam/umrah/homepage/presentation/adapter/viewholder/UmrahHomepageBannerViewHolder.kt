package com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder

import android.content.res.Resources
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageBannerEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageBannerMapper
import com.tokopedia.salam.umrah.homepage.presentation.fragment.UmrahHomepageFragment
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import kotlinx.android.synthetic.main.partial_umrah_home_page_banner.view.*
import kotlinx.android.synthetic.main.partial_umrah_home_page_banner_shimmering.view.*

class UmrahHomepageBannerViewHolder(view: View, private val onBindListener: onItemBindListener) :
        AbstractViewHolder<UmrahHomepageBannerEntity>(view) {
    override fun bind(element: UmrahHomepageBannerEntity) {
        with(itemView) {
            if (element.isLoaded && element.umrahBanners.isNotEmpty()) {

                umrah_banner_shimmering.hide()
                banner_umrah_home_page.apply {
                    show()

                    customWidth = getWidthPx(resources).toInt()
                    customHeight = getHeightPx(resources)

                    setBannerIndicator(GREEN_INDICATOR)
                    bannerSeeAll.gone()
                    val listImageUrl = UmrahHomepageBannerMapper.bannerMappertoString(element.umrahBanners)
                    setPromoList(listImageUrl)
                    setOnPromoClickListener {
                        onBindListener.onClickBanner(element.umrahBanners[it], it)
                        RouteManager.route(context, element.umrahBanners[it].applinkUrl)
                    }
                    setOnPromoScrolledListener {
                        if (!element.umrahBanners[it].isViewed) {
                            onBindListener.onImpressionBanner(element.umrahBanners[it], it)
                            element.umrahBanners[it].isViewed = true
                        }
                    }
                    buildView()
                }
                onBindListener.onPerformanceHomepageListener()

            } else if (element.isLoaded && element.umrahBanners.isEmpty()) {
                umrah_banner_shimmering.gone()
                banner_umrah_home_page.gone()
            } else {
                umrah_banner_shimmering.show()
                iv_umrah_banner_salam.apply {
                    requestLayout()
                    layoutParams.height = getHeightPx(resources)
                }
                banner_umrah_home_page.hide()
                if (!UmrahHomepageFragment.isRequestedBanner) {
                    onBindListener.onBindBannerVH(element.isLoadFromCloud)
                    UmrahHomepageFragment.isRequestedBanner = true
                } else {

                }
            }
        }
    }

    private fun getWidthPx(resources: Resources):Float{
        return (Resources.getSystem().displayMetrics.widthPixels - resources.getDimension(R.dimen.margin_right_umrah_banner).toInt()).toFloat()
    }

    private fun getHeightPx(resources: Resources):Int{
        return (getWidthPx(resources)/3.2).toInt()
    }
    companion object {
        val LAYOUT = R.layout.partial_umrah_home_page_banner
        const val GREEN_INDICATOR = 1
    }
}