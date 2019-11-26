package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.banner.BannerView
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.officialstore.ApplinkConstant
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.analytics.OfficialStoreTracking
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

    override fun onPromoClick(position: Int) {
        val bannerItem = elementBanner?.banner?.getOrNull(position)
        bannerItem?.let {
            officialStoreTracking?.eventClickBanner(
                    elementBanner?.categoryName.toEmptyStringIfNull(),
                    position,
                    it)
        }

        elementBanner?.banner?.let {
            RouteManager.route(itemView.context, it[position].applink)
        }
    }

    override fun onPromoAllClick() {
        officialStoreTracking?.eventClickAllBanner(
                elementBanner?.categoryName.toEmptyStringIfNull())

        RouteManager.route(itemView.context, ApplinkConstant.OFFICIAL_PROMO_NATIVE)
    }

    override fun onPromoDragEnd() {}

    override fun onPromoDragStart() {}

    override fun onPromoScrolled(position: Int) {
        if (position < elementBanner?.banner?.size!!) {
            val bannerItem = elementBanner?.banner?.get(position)
            officialStoreTracking?.eventImpressionBanner(
                    elementBanner?.categoryName.toEmptyStringIfNull(),
                    position,
                    bannerItem
            )
        }
    }

    override fun onPromoLoaded() {
        this.banner?.bannerIndicator?.visibility = View.VISIBLE
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.viewmodel_official_banner
    }
}
