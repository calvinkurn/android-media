package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.banner.BannerView
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.analytics.OfficialStoreTracking
import com.tokopedia.officialstore.official.data.model.Banner
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialBannerViewModel
import com.tokopedia.officialstore.official.presentation.widget.BannerOfficialStore

class OfficialBannerViewHolder(view: View?): AbstractViewHolder<OfficialBannerViewModel>(view),
        BannerView.OnPromoClickListener, BannerView.OnPromoAllClickListener,
        BannerView.OnPromoDragListener, BannerView.OnPromoScrolledListener,
        BannerView.OnPromoLoadedListener {

    private var banner: BannerOfficialStore? = null
    private var elementBanner: OfficialBannerViewModel? = null

    private var officialStoreTracking: OfficialStoreTracking? = null

    init {
        banner = view?.findViewById(R.id.banner_official)
        itemView.context?.let {
            officialStoreTracking = OfficialStoreTracking(it)
        }
    }

    override fun bind(element: OfficialBannerViewModel?) {
        elementBanner = element
        banner?.setPromoList(element?.getBannerImgUrl())
        banner?.onPromoAllClickListener = this
        banner?.onPromoScrolledListener = this
        banner?.setOnPromoLoadedListener(this)
        banner?.setOnPromoDragListener(this)
        banner?.onPromoClickListener = this
        banner?.buildView()
    }

    override fun onPromoClick(p0: Int) {
        val bannerItem = elementBanner?.banner?.get(p0)
        officialStoreTracking?.eventClickBanner(
                elementBanner?.categoryName.toEmptyStringIfNull(),
                bannerItem?.bannerId.toEmptyStringIfNull(),
                p0,
                bannerItem?.title.toEmptyStringIfNull(),
                bannerItem?.imageUrl.toEmptyStringIfNull())

        elementBanner?.banner?.let {
            RouteManager.route(itemView.context, it[p0].applink)
        }
    }

    override fun onPromoAllClick() {
        // TODO add on banner see all click
    }

    override fun onPromoDragEnd() {}

    override fun onPromoDragStart() {}

    override fun onPromoScrolled(position: Int) {}

    override fun onPromoLoaded() {}

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.viewmodel_official_banner
    }

}