package com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.dynamic.BannerViewDynamicBackground
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageBannerModel
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment
import com.tokopedia.travelhomepage.homepage.presentation.listener.ActivityStateListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.travel_homepage_banner.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageBannerViewHolder(itemView: View, private val onBindListener: OnItemBindListener,
                                     private val onItemClickListener: OnItemClickListener)
    : AbstractViewHolder<TravelHomepageBannerModel>(itemView), BannerView.OnPromoClickListener, BannerView.OnPromoScrolledListener,
        BannerView.OnPromoAllClickListener, BannerView.OnPromoLoadedListener, BannerView.OnPromoDragListener, ActivityStateListener {

    private val bannerView: BannerViewDynamicBackground = itemView.findViewById(R.id.banner)
    private lateinit var bannerList: List<TravelCollectiveBannerModel.Banner>
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
                bannerView.shouldShowSeeAllButton(bannerList.isNotEmpty())

                val promoUrls = arrayListOf<String>()
                showAllUrl = element.travelCollectiveBannerModel.meta.appUrl
                for (slidesModel in bannerList) {
                    promoUrls.add(slidesModel.attribute.imageUrl)
                }
                bannerView.setPromoList(promoUrls)
                bannerView.buildView()
            } catch (e: Throwable) {

            }
        } else onBindListener.onBannerVHItemBind(element.isLoadFromCloud)

        itemView.banner_shimmering.visibility = if (element.isLoaded) View.GONE else View.VISIBLE
    }

    override fun onPromoLoaded() {

    }

    override fun onPromoClick(position: Int) {
        onItemClickListener.onTrackBannerClick(bannerList[position], position + 1)
        onItemClickListener.onItemClick(bannerList[position].attribute.appUrl, bannerList[position].attribute.webUrl)
    }

    override fun onPromoScrolled(position: Int) {
        onItemClickListener.onTrackBannerImpression(bannerList[position], position + 1)
    }

    override fun onPromoAllClick() {
        onItemClickListener.onTrackEventClick(TravelHomepageFragment.TYPE_ALL_PROMO)
        onItemClickListener.onItemClick(showAllUrl)
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