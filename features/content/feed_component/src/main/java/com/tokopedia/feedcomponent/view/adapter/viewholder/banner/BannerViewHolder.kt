package com.tokopedia.feedcomponent.view.adapter.viewholder.banner

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import kotlinx.android.synthetic.main.item_banner.view.*

/**
 * @author by milhamj on 08/05/18.
 */

class BannerViewHolder(v: View) : AbstractViewHolder<BannerViewModel>(v) {

    private val adapter: BannerAdapter = BannerAdapter()

    init {
        itemView.bannerRv.adapter = adapter
    }

    override fun bind(element: BannerViewModel) {
        adapter.setData(element.itemViewModels)

        itemView.cardTitle.bind(element.title, element.template.cardbanner.title)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_banner
    }
}
