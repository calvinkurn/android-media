package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.banner.BannerView
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.widget.BannerShopPage
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

/**
 * Created by rizqiaryansa on 2020-02-25.
 */

class ShopHomeSliderBannerViewHolder(view: View?): AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(view),
        BannerView.OnPromoClickListener, BannerView.OnPromoAllClickListener,
        BannerView.OnPromoDragListener, BannerView.OnPromoScrolledListener,
        BannerView.OnPromoLoadedListener{

    private var banner: BannerShopPage? = null

    init {
        banner = view?.findViewById(R.id.banner_shop_page)
    }

    override fun bind(element: ShopHomeDisplayWidgetUiModel) {
        banner?.setPromoList(dataWidgetToString(element))
        banner?.onPromoAllClickListener = this
        banner?.onPromoScrolledListener = this
        banner?.setOnPromoLoadedListener(this)
        banner?.setOnPromoDragListener(this)
        banner?.onPromoClickListener = this
        banner?.buildView()
    }

    override fun onPromoLoaded() {
        this.banner?.bannerIndicator?.visible()
    }

    override fun onPromoClick(p0: Int) {}

    override fun onPromoAllClick() {}

    override fun onPromoDragEnd() {}

    override fun onPromoDragStart() {}

    override fun onPromoScrolled(p0: Int) {}

    private fun dataWidgetToString(element: ShopHomeDisplayWidgetUiModel): List<String>? {
        val mutableString: MutableList<String>? = mutableListOf()
        element.data?.map {
            it.imageUrl?.let { img ->
                mutableString?.add(img)
            }
        }
        return mutableString?.toList()
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.viewmodel_slider_banner
    }

}