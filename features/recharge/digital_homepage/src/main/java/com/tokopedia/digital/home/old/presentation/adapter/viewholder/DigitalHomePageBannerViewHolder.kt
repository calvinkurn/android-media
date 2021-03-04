package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.banner.BannerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.old.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_banner.view.*

class DigitalHomePageBannerViewHolder(val view : View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageBannerModel>(view), BannerView.OnPromoAllClickListener,
        BannerView.OnPromoClickListener, BannerView.OnPromoScrolledListener, BannerView.OnPromoLoadedListener, BannerView.OnPromoDragListener {

    override fun onPromoDragStart() {
        //do nothing
    }

    override fun onPromoDragEnd() {
        //do nothing
    }

    override fun onPromoLoaded() {
        //do nothing
    }

    override fun onPromoScrolled(position: Int) {
        onItemBindListener.onBannerImpressionTrack(bannerList?.get(position), position+1)
    }

    override fun onPromoClick(position: Int) {
        onItemBindListener.onBannerItemClicked(bannerList?.get(position), position)
    }

    override fun onPromoAllClick() {
        onItemBindListener.onBannerAllItemClicked()
    }

    private var bannerList: List<DigitalHomePageBannerModel.Banner>? = listOf()

    override fun bind(element: DigitalHomePageBannerModel?) {
        itemView.banner?.onPromoAllClickListener = this
        itemView.banner?.onPromoClickListener = this
        itemView.banner?.onPromoScrolledListener = this
        itemView.banner?.setOnPromoLoadedListener(this)
        itemView.banner?.setOnPromoDragListener(this)
        if (element?.isLoaded == true) {
            itemView.banner_shimmering.hide()
            try {
                bannerList = element.bannerList
                itemView.banner.shouldShowSeeAllButton(bannerList?.isNotEmpty() ?: false)

                val promoUrls = arrayListOf<String>()
                for (slidesModel in bannerList ?: listOf()) {
                    promoUrls.add(slidesModel.filename)
                }
                itemView.banner.setPromoList(promoUrls)
                itemView.banner.buildView()
            } catch (e: Throwable) {

            }
        } else{
            itemView.banner_shimmering.show()
        }
    }

    companion object{
        val LAYOUT =  R.layout.layout_digital_home_banner
    }
}