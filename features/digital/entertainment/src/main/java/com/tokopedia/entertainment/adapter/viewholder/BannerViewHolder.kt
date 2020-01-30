package com.tokopedia.entertainment.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.Indicator
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.adapter.HomeViewHolder
import com.tokopedia.entertainment.adapter.viewmodel.BannerViewModel
import kotlinx.android.synthetic.main.ent_banner_view.view.*

/**
 * Author errysuprayogi on 27,January,2020
 */
class BannerViewHolder: HomeViewHolder<BannerViewModel>, BannerView.OnPromoClickListener,
BannerView.OnPromoAllClickListener, BannerView.OnPromoScrolledListener,
BannerView.OnPromoDragListener, BannerView.OnPromoLoadedListener {

    constructor(itemView: View) : super(itemView)

    init {
        var listPromo = List<String>(5) { index: Int ->
            "https://ecs7.tokopedia.net/img/attachment/2020/1/1/42484317/42484317_ddeaa295-aef8-4705-9d4e-1a2adc91581c.jpg"
        }
        var context = itemView.context;
        itemView.banner_home_ent?.setPromoList(listPromo)
        itemView.banner_home_ent?.setOnPromoClickListener(this)
        itemView.banner_home_ent?.setOnPromoAllClickListener(this)
        itemView.banner_home_ent?.onPromoScrolledListener = this
        itemView.banner_home_ent?.setOnPromoDragListener(this)
        itemView.banner_home_ent?.customWidth = context.resources.getDimensionPixelSize(R.dimen.banner_item_width)
        itemView.banner_home_ent?.customHeight = context.resources.getDimensionPixelSize(R.dimen.banner_item_height)
        itemView.banner_home_ent?.setBannerSeeAllTextColor(ContextCompat.getColor(context, R.color.ent_green))
        itemView.banner_home_ent?.setBannerIndicator(Indicator.GREEN)
        itemView.banner_home_ent?.buildView()
    }

    override fun onPromoClick(p0: Int) {

    }

    override fun onPromoAllClick() {
    }

    override fun onPromoScrolled(pos: Int) {
    }

    override fun onPromoDragEnd() {
    }

    override fun onPromoDragStart() {
    }

    override fun onPromoLoaded() {
    }

    override fun bind(element: BannerViewModel) {

    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_banner_view
    }
}