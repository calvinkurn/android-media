package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeSwipeBannerBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeHomepageSwipeBannerModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener

/**
 * @author: astidhiyaa on 01/11/21.
 */
class RechargeHomepageSwipeBannerViewHolder(itemView: View,
                                            val listener: RechargeHomepageItemListener)
    : AbstractViewHolder<RechargeHomepageSwipeBannerModel>(itemView), CarouselUnify.OnActiveIndexChangedListener {

    private lateinit var slidesList: List<RechargeHomepageSections.Item>

    override fun bind(element: RechargeHomepageSwipeBannerModel) {
        val bind = ViewRechargeHomeSwipeBannerBinding.bind(itemView)
        slidesList = element.section.items
        try {
            initBanner(bind, element)
        }catch (exception: Exception){
            exception.printStackTrace()
        }
    }

    private fun initBanner(bind: ViewRechargeHomeSwipeBannerBinding, element: RechargeHomepageSwipeBannerModel){
        bind.rechargeHomeSwipeBanner.apply {
            freeMode = false
            centerMode = true
            slideToScroll = 1
            slideToShow = 1.0f
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            onItemClick = {
                listener.onRechargeSectionItemClicked(slidesList[it])
            }

            if(element.section.items.isNotEmpty()){
                autoplayDuration = 5000L
                autoplay = true
                infinite = true
            }
            else {
                listener.loadRechargeSectionData(element.visitableId())
            }

            val urlArr : ArrayList<String> = slidesList.map {
                it.mediaUrl
            } as ArrayList<String>
            addBannerImages(urlArr)
        }
    }

    override fun onActiveIndexChanged(prev: Int, current: Int) {
        if(current >= 0 && current < slidesList.size){
            listener.onRechargeSectionItemImpression(slidesList[current])
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_swipe_banner
    }
}