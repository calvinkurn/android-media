package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.adapter.ExploreBannerAdapter
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreBannerViewModel
import kotlinx.android.synthetic.main.item_af_banner.view.*

/**
 * @author by milhamj on 18/03/19.
 */
class ExploreBannerViewHolder(v: View) : AbstractViewHolder<ExploreBannerViewModel>(v) {

    val adapter: ExploreBannerAdapter by lazy {
        ExploreBannerAdapter()
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_af_popular_profile
    }

    override fun bind(element: ExploreBannerViewModel?) {
        if (element == null) {
            return
        }

        adapter.list.clear()
        adapter.list.addAll(element.banners)
        adapter.notifyDataSetChanged()
        itemView.bannerRv.adapter = adapter
    }
}