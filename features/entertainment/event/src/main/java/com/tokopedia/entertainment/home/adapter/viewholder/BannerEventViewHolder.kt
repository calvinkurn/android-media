package com.tokopedia.entertainment.home.adapter.viewholder

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.Indicator
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.listener.TrackingListener
import com.tokopedia.entertainment.home.adapter.viewmodel.BannerModel
import com.tokopedia.entertainment.home.fragment.NavEventHomeFragment
import kotlinx.android.synthetic.main.ent_banner_view.view.*


/**
 * Author errysuprayogi on 27,January,2020
 */
class BannerEventViewHolder(itemView: View, val listener: TrackingListener) : AbstractViewHolder<BannerModel>(itemView), BannerView.OnPromoClickListener,
        BannerView.OnPromoAllClickListener, BannerView.OnPromoScrolledListener,
        BannerView.OnPromoDragListener, BannerView.OnPromoLoadedListener {

    var context: Context
    var el: BannerModel? = null

    init {
        context = itemView.context
        itemView.banner_home_ent?.onPromoClickListener = this
        itemView.banner_home_ent?.onPromoAllClickListener = this
        itemView.banner_home_ent?.setOnPromoScrolledListener(this)
        itemView.banner_home_ent?.setOnPromoLoadedListener(this)
        itemView.banner_home_ent?.setOnPromoDragListener(this)
        itemView.banner_home_ent?.setBannerSeeAllTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
        itemView.banner_home_ent?.setBannerIndicator(Indicator.GREEN)
    }

    override fun onPromoClick(p: Int) {
        el?.let {
            listener.clickBanner(it.layout.items.get(p), p)
            RouteManager.route(context, it.layout.items.get(p).appUrl)
        }
    }

    override fun onPromoAllClick() {
        RouteManager.route(context, NavEventHomeFragment.PROMOURL)
    }

    override fun onPromoScrolled(pos: Int) {
        el?.let {
            listener.impressionBanner(it.layout.items.get(pos), pos)
        }
    }

    override fun onPromoDragEnd() {
    }

    override fun onPromoDragStart() {

    }

    override fun onPromoLoaded() {
        el?.let {
            if (it.items.size == 1) {
                listener.impressionBanner(it.layout.items.get(0), 0)
            }
        }
    }

    override fun bind(element: BannerModel) {
        el = element
        itemView.banner_home_ent?.setPromoList(element.items)
        if (element.items.size == 1) {
            itemView.viewTreeObserver.addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            itemView.banner_home_ent?.apply {
                                customWidth = itemView.measuredWidth - itemView.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl4)
                                buildView()
                            }
                        }
                    })
        }

        itemView.banner_home_ent?.buildView()

    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_banner_view
    }
}
