package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.app.Activity
import android.util.DisplayMetrics
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
import com.tokopedia.unifycomponents.ImageUnify

/**
 * @author: astidhiyaa on 01/11/21.
 */
class RechargeHomepageSwipeBannerViewHolder(itemView: View,
                                            val listener: RechargeHomepageItemListener)
    : AbstractViewHolder<RechargeHomepageSwipeBannerModel>(itemView){

    private lateinit var slidesList: List<RechargeHomepageSections.Item>
    private var urlArr: ArrayList<Any> = ArrayList()

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
            centerMode = true
            slideToScroll = SLIDE_TO_SCROLL
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            slideToShow = SLIDE_TO_SHOW

            if(slidesList.size == 1){
                autoplay = false
                infinite = false
            }
            else {
                autoplayDuration = AUTOPLAY_DURATION
                autoplay = true
                infinite = true
            }


            val itemParam = { view: View, data: Any ->
                data as BannerData

                val img = view.findViewById<ImageUnify>(R.id.recharge_img_swipe_banner)

                this.post {
                    img.layoutParams.width = getBannerWidth()
                }

                img.setImageUrl(data.mediaUrl)

                val index = urlArr.indexOf(data)
                img.setOnClickListener {
                    if(::slidesList.isInitialized) listener.onRechargeSectionItemClicked(slidesList[index])
                }
            }

            stage.removeAllViews()
            if (stage.childCount == 0){
                slidesList.map {
                    urlArr.add(BannerData(it.mediaUrl))
                }
                addItems(R.layout.view_recharge_swipe_banner_image, urlArr, itemParam)
                bind.root.post {
                    activeIndex = 0
                }
            }
        }
    }

    private fun getBannerWidth(): Int{
        val displayMetrics = DisplayMetrics()
        (itemView.context as Activity).windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        var bannerWidth = (displayMetrics.widthPixels / SLIDE_TO_SHOW).toInt()
        bannerWidth -= MARGIN_SIZE
        return bannerWidth
    }

    class BannerData(var mediaUrl: String)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_swipe_banner

        private const val AUTOPLAY_DURATION = 5000L
        private const val SLIDE_TO_SHOW = 1.2f
        private const val SLIDE_TO_SCROLL = 1
        private const val MARGIN_SIZE = 16
    }
}