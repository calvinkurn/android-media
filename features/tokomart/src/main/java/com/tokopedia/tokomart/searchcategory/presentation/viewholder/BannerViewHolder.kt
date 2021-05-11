package com.tokopedia.tokomart.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.common.base.listener.BannerListener
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView
import kotlinx.android.synthetic.main.item_tokomart_search_category_banner.view.*

class BannerViewHolder(
        itemView: View,
        private val bannerListener: BannerListener
): AbstractViewHolder<BannerDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_search_category_banner
    }

    override fun bind(element: BannerDataView?) {
        val dummyUrl = arrayListOf("https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/7/23/20723472/20723472_6789d6a3-2599-40a7-9740-b32720a1f60c.png")
        itemView.carousel_category_banner.run {
            addBannerImages(dummyUrl)
            setOnClickListener {
                bannerListener.onBannerClick(
                        "tokopedia://digital/form?category_id=6&menu_id=4&template=voucher")
            }
        }
    }
}
