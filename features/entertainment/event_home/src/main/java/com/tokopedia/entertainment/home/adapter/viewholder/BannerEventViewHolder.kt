package com.tokopedia.entertainment.home.adapter.viewholder

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.Indicator
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.BannerViewModel
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking
import com.tokopedia.entertainment.home.fragment.EventHomeFragment
import kotlinx.android.synthetic.main.ent_banner_view.view.*


/**
 * Author errysuprayogi on 27,January,2020
 */
class BannerEventViewHolder: HomeEventViewHolder<BannerViewModel>, BannerView.OnPromoClickListener,
BannerView.OnPromoAllClickListener, BannerView.OnPromoScrolledListener,
BannerView.OnPromoDragListener, BannerView.OnPromoLoadedListener {

    constructor(itemView: View) : super(itemView)
    var context : Context
    var el: BannerViewModel? = null

    init {
        context = itemView.context
        itemView.banner_home_ent?.onPromoClickListener = this
        itemView.banner_home_ent?.onPromoAllClickListener = this
        itemView.banner_home_ent?.onPromoScrolledListener = this
        itemView.banner_home_ent?.setOnPromoDragListener(this)
        itemView.banner_home_ent?.customWidth = getDisplayMetric(context).widthPixels -
                        context.resources.getDimensionPixelSize(R.dimen.banner_item_margin)
        itemView.banner_home_ent?.customHeight = context.resources.getDimensionPixelSize(R.dimen.banner_item_height)
        itemView.banner_home_ent?.setBannerSeeAllTextColor(ContextCompat.getColor(context, R.color.ent_green))
        itemView.banner_home_ent?.setBannerIndicator(Indicator.GREEN)
    }

    private fun getDisplayMetric(context: Context): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    override fun onPromoClick(p: Int) {
        el?.let {
            RouteManager.route(context, it.layout.items.get(p).url)
            EventHomePageTracking.getInstance().clickBanner(it.layout.items.get(p), p)
        }
    }

    override fun onPromoAllClick() {
        RouteManager.route(context, EventHomeFragment.PROMOURL)
    }

    override fun onPromoScrolled(pos: Int) {
        el?.let {
            EventHomePageTracking.getInstance().impressionBanner(it.layout.items.get(pos), pos)
        }
    }

    override fun onPromoDragEnd() {
    }

    override fun onPromoDragStart() {
    }

    override fun onPromoLoaded() {
    }

    override fun bind(element: BannerViewModel) {
        el = element
        itemView.banner_home_ent?.setPromoList(element.items)
        itemView.banner_home_ent?.buildView()
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_banner_view
    }
}