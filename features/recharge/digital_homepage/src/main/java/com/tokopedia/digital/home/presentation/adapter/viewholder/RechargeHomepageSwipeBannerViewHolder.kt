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
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener

/**
 * @author: astidhiyaa on 01/11/21.
 */
class RechargeHomepageSwipeBannerViewHolder(itemView: View,
                                            val listener: RechargeHomepageItemListener)
    : AbstractViewHolder<RechargeHomepageSwipeBannerModel>(itemView){

    private lateinit var slidesList: List<RechargeHomepageSections.Item>

    override fun bind(element: RechargeHomepageSwipeBannerModel) {
        val bind = ViewRechargeHomeSwipeBannerBinding.bind(itemView)
        slidesList = element.section.items
        try {
            if (slidesList.isNotEmpty()){
                initBanner(bind)
                bind.root.addOnImpressionListener(element.section){
                    listener.onRechargeSectionItemImpression(element.section)
                }
            }else{
                listener.loadRechargeSectionData(element.visitableId())
            }
        }catch (exception: Exception){
            exception.printStackTrace()
        }
    }

    private fun initBanner(bind: ViewRechargeHomeSwipeBannerBinding){
        bind.rechargeHomeSwipeBanner.apply {
            freeMode = false
            centerMode = true
            slideToScroll = 1
            slideToShow = 1.0f
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            onItemClick = {
                if(::slidesList.isInitialized) listener.onRechargeSectionItemClicked(slidesList[it])
            }

            if(slidesList.size == 1){
                autoplay = false
            }
            else {
                autoplayDuration = AUTOPLAY_DURATION
                autoplay = true
            }

            val urlArr : ArrayList<String> = slidesList.map {
                it.mediaUrl
            } as ArrayList<String>
            addBannerImages(urlArr)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_swipe_banner

        const val AUTOPLAY_DURATION = 5000L
    }
}