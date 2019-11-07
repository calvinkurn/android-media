package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.dynamicbanner.entity.PlayCard
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.showWithCondition

class PlayCardViewHolder(val view: View, val listener: HomeCategoryListener): AbstractViewHolder<PlayCardViewModel>(view) {

    private val rootBanner = view.findViewById<RelativeLayout>(R.id.bannerPlay)
    private val imgBanner = view.findViewById<ImageView>(R.id.imgBanner)
    private val chipPlayLive = view.findViewById<LinearLayout>(R.id.chipPlayLive)
    private val chipPlayViewers = view.findViewById<LinearLayout>(R.id.chipPlayViewers)
    private val txtTotalViewers = view.findViewById<TextView>(R.id.txtTotalViewers)

    override fun bind(element: PlayCardViewModel) {
        listener.onGetPlayBanner(adapterPosition)
        bindCard(element.getPlayCardHome().playGetCardHome)
        rootBanner.setOnClickListener {
            element.getChannel()?.enhanceClickPlayBanner
            RouteManager.route(view.context, element.getPlayCardHome().playGetCardHome.applink)
        }
    }

    private fun bindCard(card: PlayCard) {
        imgBanner.loadImageRounded(card.imageUrl, 10f)
        txtTotalViewers.text = card.totalView
        chipLive(card)
        chipViewers(card)
    }

    private fun chipLive(card: PlayCard) {
        chipPlayLive.showWithCondition(card.isShowLive)
    }

    private fun chipViewers(card: PlayCard) {
        chipPlayViewers.showWithCondition(card.isShowTotalView)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_home_play_card
    }

}