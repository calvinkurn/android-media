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
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.unifycomponents.toPx

/**
 * @author: astidhiyaa on 01/11/21.
 */
class RechargeHomepageSwipeBannerViewHolder(itemView: View,
                                            val listener: RechargeHomepageItemListener)
    : AbstractViewHolder<RechargeHomepageSwipeBannerModel>(itemView), CarouselUnify.OnActiveIndexChangedListener {

    private var slidesList: List<RechargeHomepageSections.Item> = emptyList()

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
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN

            when {
                slidesList.isNotEmpty() -> {
                    slideToShow = 1.3f
                    autoplay = true
                    infinite = true
                    setMargin(left = 12.toPx(), top = 8.toPx(), bottom = 0, right = 12.toPx())
                }
                slidesList.size == 1 -> {
                    autoplay = false
                    infinite = false
                    slideToShow = 1.0f
                }
                else -> listener.loadRechargeSectionData(element.visitableId())
            }
            val urlArr : ArrayList<String> = slidesList.map {
                it.mediaUrl
            } as ArrayList<String>
            addBannerImages(urlArr)
        }
    }

    override fun onActiveIndexChanged(prev: Int, current: Int) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_swipe_banner
    }
}