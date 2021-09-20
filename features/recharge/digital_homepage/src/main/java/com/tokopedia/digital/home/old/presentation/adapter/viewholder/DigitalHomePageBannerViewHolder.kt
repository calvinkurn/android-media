package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.banner.BannerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeBannerBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

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
        view?.let {
            val binding = LayoutDigitalHomeBannerBinding.bind(it)
            binding.banner.onPromoAllClickListener = this
            binding.banner.onPromoClickListener = this
            binding.banner.onPromoScrolledListener = this
            binding.banner.setOnPromoLoadedListener(this)
            binding.banner.setOnPromoDragListener(this)
            if (element?.isLoaded == true) {
                binding.bannerShimmering.rootView.hide()
                try {
                    bannerList = element.bannerList
                    binding.banner.shouldShowSeeAllButton(bannerList?.isNotEmpty() ?: false)

                    val promoUrls = arrayListOf<String>()
                    for (slidesModel in bannerList ?: listOf()) {
                        promoUrls.add(slidesModel.filename)
                    }
                    binding.banner.setPromoList(promoUrls)
                    binding.banner.buildView()
                } catch (e: Throwable) {

                }
            } else{
                binding.bannerShimmering.rootView.show()
            }
        }

    }

    companion object{
        val LAYOUT =  R.layout.layout_digital_home_banner
    }
}