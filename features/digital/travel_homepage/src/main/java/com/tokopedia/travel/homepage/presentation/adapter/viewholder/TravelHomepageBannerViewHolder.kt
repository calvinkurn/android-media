package com.tokopedia.travel.homepage.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.banner.BannerView
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageBannerModel
import com.tokopedia.travel.homepage.presentation.listener.ActivityStateListener
import com.tokopedia.travel.homepage.presentation.customview.TravelHomepageBannerViewDynamicBackground
import com.tokopedia.travel.homepage.presentation.listener.OnItemBindListener
import kotlinx.android.synthetic.main.travel_homepage_banner.view.*

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageBannerViewHolder(itemView: View, val onBindListener: OnItemBindListener) : AbstractViewHolder<TravelHomepageBannerModel>(itemView), BannerView.OnPromoClickListener, BannerView.OnPromoScrolledListener,
        BannerView.OnPromoAllClickListener, BannerView.OnPromoLoadedListener, BannerView.OnPromoDragListener, ActivityStateListener {

    private val bannerView: TravelHomepageBannerViewDynamicBackground = itemView.findViewById(R.id.banner)
    private val context: Context = itemView.context
    private lateinit var bannerList: List<TravelHomepageBannerModel.Banner>

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
                bannerList = element.banners
                bannerView.shouldShowSeeAllButton(bannerList.isNotEmpty())

                val promoUrls = arrayListOf<String>()
                for (slidesModel in bannerList) {
                    promoUrls.add(slidesModel.attribute.imageUrl)
                }
                bannerView.setPromoList(promoUrls)
                bannerView.buildView()
            } catch (e: Throwable) {

            }
        } else onBindListener.onBannerVHItemBind()

        itemView.banner_shimmering.visibility = if (element.isLoaded) View.GONE else View.VISIBLE
    }

    override fun onPromoLoaded() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPromoClick(position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPromoScrolled(position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPromoAllClick() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPromoDragStart() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPromoDragEnd() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPause() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val LAYOUT = R.layout.travel_homepage_banner
    }
}