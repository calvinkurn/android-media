package com.tokopedia.digital.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.banner.BannerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.presentation.customview.TravelHomepageBannerViewDynamicBackground
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_banner.view.*

class DigitalHomePageBannerViewHolder(val view : View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageBannerModel>(view), BannerView.OnPromoAllClickListener,
        BannerView.OnPromoClickListener{

    override fun onPromoClick(position: Int) {
        onItemBindListener.onBannerItemClicked(bannerList?.get(position))
    }

    override fun onPromoAllClick() {
        onItemBindListener.onBannerAllItemClicked()
    }

    private val adapterBanner  by lazy {
        DigitalItemBannerAdapter()
    }

    private var bannerList: List<DigitalHomePageBannerModel.Banner>? = listOf()

    override fun bind(element: DigitalHomePageBannerModel?) {
        itemView.banner?.onPromoAllClickListener = this
        itemView.banner?.onPromoClickListener = this
        if (element?.isLoaded?:false) {
            itemView.banner_shimmering.hide()
            try {
                bannerList = element?.bannerList
                itemView.banner.shouldShowSeeAllButton(bannerList?.isNotEmpty()?:false)

                val promoUrls = arrayListOf<String>()
                for (slidesModel in bannerList?: listOf()) {
                    promoUrls.add(slidesModel.imgUrl)
                }
                itemView.banner.setPromoList(promoUrls)
                itemView.banner.buildView()
            } catch (e: Throwable) {

            }
        } else{
            itemView.banner_shimmering.show()
            onItemBindListener.onBannerItemDigitalBind()
        }
    }

    companion object{
        val LAYOUT =  R.layout.layout_digital_home_banner
    }
}