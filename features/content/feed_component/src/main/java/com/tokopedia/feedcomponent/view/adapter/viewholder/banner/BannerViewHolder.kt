package com.tokopedia.feedcomponent.view.adapter.viewholder.banner

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import kotlinx.android.synthetic.main.item_banner.view.*

/**
 * @author by milhamj on 08/05/18.
 */

class BannerViewHolder(v: View, 
                       private val listener: BannerAdapter.BannerItemListener,
                       private var cardTitleListener: CardTitleView.CardTitleListener)
    : AbstractViewHolder<BannerViewModel>(v) {

    override fun bind(element: BannerViewModel) {
        itemView.bannerRv.adapter = BannerAdapter(element.itemViewModels, adapterPosition, listener)

        itemView.cardTitle.bind(element.title, element.template.cardbanner.title)
        itemView.cardTitle.listener = cardTitleListener
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_banner
    }
}
