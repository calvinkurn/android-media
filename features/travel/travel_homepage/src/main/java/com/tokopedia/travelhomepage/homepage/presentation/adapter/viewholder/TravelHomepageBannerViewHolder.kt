package com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder

import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.dynamic.BannerViewDynamicBackground
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageBannerModel
import com.tokopedia.travelhomepage.homepage.presentation.listener.ActivityStateListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.TravelHomepageActionListener
import kotlinx.android.synthetic.main.travel_homepage_banner.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageBannerViewHolder(itemView: View, private val onBindListener: OnItemBindListener,
                                     private val travelHomepageActionListener: TravelHomepageActionListener)
    : AbstractViewHolder<TravelHomepageBannerModel>(itemView), BannerView.OnPromoClickListener, BannerView.OnPromoScrolledListener,
        BannerView.OnPromoAllClickListener, BannerView.OnPromoLoadedListener, BannerView.OnPromoDragListener, ActivityStateListener {

    private val bannerView: BannerViewDynamicBackground = itemView.findViewById(R.id.banner)
    private lateinit var bannerList: List<TravelCollectiveBannerModel.Banner>
    private var currentPosition = -1
    private var showAllUrl: String = ""

    init {
        bannerView.onPromoAllClickListener = this
        bannerView.onPromoClickListener = this
        bannerView.onPromoScrolledListener = this
        bannerView.setOnPromoLoadedListener(this)
        bannerView.setOnPromoDragListener(this)
    }

    override fun bind(element: TravelHomepageBannerModel) {
        if (element.isLoaded) {
            try {
                bannerList = element.travelCollectiveBannerModel.banners

                if (currentPosition != element.layoutData.position) {
                    val promoUrls = arrayListOf<String>()
                    showAllUrl = element.travelCollectiveBannerModel.meta.appUrl

                    for (slidesModel in bannerList) {
                        promoUrls.add(slidesModel.attribute.imageUrl)
                    }
                    if (element.layoutData.appUrl.isNotEmpty()) bannerView.bannerSeeAll.text = element.layoutData.metaText
                    else bannerView.bannerSeeAll.text = ""
                    bannerView.setPromoList(promoUrls)
                    bannerView.buildView()
                    bannerView.bannerSeeAll.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f)
                    bannerView.bannerSeeAll.setTypeface(null, Typeface.BOLD)

                    currentPosition = element.layoutData.position
                }
            } catch (e: Throwable) {

            }
        } else {
            currentPosition = -1
            onBindListener.onBannerItemBind(element.layoutData, element.isLoadFromCloud)
        }

        itemView.banner_shimmering.visibility = if (element.isLoaded) View.GONE else View.VISIBLE
    }

    override fun onPromoLoaded() {

    }

    override fun onPromoClick(position: Int) {
        bannerList.getOrNull(position)?.let {
            travelHomepageActionListener.onClickSliderBannerItem(it, position)
            travelHomepageActionListener.onItemClick(it.attribute.appUrl, it.attribute.webUrl)
        }
    }

    override fun onPromoScrolled(position: Int) {
        bannerList.getOrNull(position)?.let {
            travelHomepageActionListener.onViewSliderBanner(it, position)
        }
    }

    override fun onPromoAllClick() {
        travelHomepageActionListener.onClickSeeAllSliderBanner()
        travelHomepageActionListener.onItemClick(showAllUrl)
    }

    override fun onPromoDragStart() {

    }

    override fun onPromoDragEnd() {

    }

    override fun onPause() {

    }

    override fun onResume() {

    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_banner
    }
}