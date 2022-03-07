package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeSwipeBannerBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeHomepageSwipeBannerModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.unifycomponents.toPx

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
            val urlArr = slidesList.map {
                it.mediaUrl
            }
            setPromoList(urlArr)
            setOnPromoClickListener { position ->
                if (::slidesList.isInitialized)
                    listener.onRechargeSectionItemClicked(slidesList[position])
            }
            setOnPromoAllClickListener {  }
            setOnPromoScrolledListener {  }
            setOnPromoLoadedListener {
                bannerIndicator.gone()
                bannerSeeAll.gone()
            }

            customWidth = SWIPE_BANNER_WIDTH.toPx()
            customHeight = SWIPE_BANNER_HEIGHT.toPx()

            buildView()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_swipe_banner

        private const val SWIPE_BANNER_WIDTH = 328
        private const val SWIPE_BANNER_HEIGHT = 109
    }
}