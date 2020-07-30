package com.tokopedia.salam.umrah.homepage.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.util.UmrahPriceUtil.getSlashedPrice
import com.tokopedia.salam.umrah.homepage.data.model.UmrahHomepageDealsWidgetModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_umrah_homepage_deals.view.*

class UmrahHomepageDealsWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var umrahHomepageDealsWidgetModel: UmrahHomepageDealsWidgetModel

    init {
        View.inflate(context, R.layout.widget_umrah_homepage_deals, this)
    }

    fun buildView() {
        if (::umrahHomepageDealsWidgetModel.isInitialized) {
            hideLoading()
            iv_umrah_image.loadImage(umrahHomepageDealsWidgetModel.topImageUrl)
            label_umrah_duration.text = umrahHomepageDealsWidgetModel.duration
            iv_umrah_provider_logo.loadImage(umrahHomepageDealsWidgetModel.providerLogoUrl)
            tg_umrah_title.text = umrahHomepageDealsWidgetModel.title
            tg_umrah_price.text = umrahHomepageDealsWidgetModel.price
            tg_umrah_calendar.text = umrahHomepageDealsWidgetModel.date
            tg_umrah_hotel.text = umrahHomepageDealsWidgetModel.hotel
            tg_umrah_plane.text = umrahHomepageDealsWidgetModel.plane
            tg_umrah_mulai_dari.text = getSlashedPrice(resources, umrahHomepageDealsWidgetModel.slashPrice)
        } else {
            showLoading()
        }
    }

    fun showLoading() {
        container_umrah_homepage_deals.visibility = View.GONE
        container_umrah_homepage_deals_shimmering.visibility = View.VISIBLE
    }

    fun hideLoading() {
        container_umrah_homepage_deals.visibility = View.VISIBLE
        container_umrah_homepage_deals_shimmering.visibility = View.GONE
    }
}