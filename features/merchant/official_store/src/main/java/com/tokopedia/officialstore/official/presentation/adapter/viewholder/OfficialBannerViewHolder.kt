package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.banner.BannerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.officialstore.ApplinkConstant
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.analytics.OfficialStoreTracking
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialBannerDataModel
import com.tokopedia.officialstore.official.presentation.widget.BannerOfficialStore
import kotlinx.android.synthetic.main.viewmodel_official_banner.view.*

class OfficialBannerViewHolder(view: View): AbstractViewHolder<OfficialBannerDataModel>(view),
        BannerView.OnPromoClickListener, BannerView.OnPromoAllClickListener,
        BannerView.OnPromoDragListener, BannerView.OnPromoScrolledListener,
        BannerView.OnPromoLoadedListener {

    private var banner: BannerOfficialStore? = null
    private var elementBanner: OfficialBannerDataModel? = null

    private var officialStoreTracking: OfficialStoreTracking? = null

    init {
        banner = view?.findViewById(R.id.banner_official)
        itemView.context?.let {
            officialStoreTracking = OfficialStoreTracking(it)
        }
    }

    override fun bind(element: OfficialBannerDataModel) {
        elementBanner = element
        itemView.banner_official.run {
            setPromoList(element.banner.map { it.imageUrl })
            setOnPromoDragListener(this@OfficialBannerViewHolder)
            setOnPromoLoadedListener(this@OfficialBannerViewHolder)
            onPromoClickListener = this@OfficialBannerViewHolder
            onPromoAllClickListener = this@OfficialBannerViewHolder
            onPromoScrolledListener = this@OfficialBannerViewHolder
            buildView()
        }

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
        if (position < elementBanner?.banner?.size.orZero()) {
            val bannerItem = elementBanner?.banner?.get(position)
            bannerItem?.let {
                officialStoreTracking?.eventImpressionBanner(
                        elementBanner?.categoryName.toEmptyStringIfNull(),
                        position,
                        it
                )
            }
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
