package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.dynamicbanner.entity.PlayCard
import com.tokopedia.dynamicbanner.entity.PlayCardHome
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition

class PlayCardViewHolder(
        val view: View,
        val listener: HomeCategoryListener
): AbstractViewHolder<PlayCardViewModel>(view) {

    private val container = view.findViewById<ConstraintLayout>(R.id.bannerPlay)
    private val imgBanner = view.findViewById<ImageView>(R.id.imgBanner)
    private val chipPlayLive = view.findViewById<LinearLayout>(R.id.chipPlayLive)
    private val chipPlayViewers = view.findViewById<LinearLayout>(R.id.chipPlayViewers)
    private val txtTotalViewers = view.findViewById<TextView>(R.id.txtTotalViewers)

    private var playCardHome: PlayCardHome? = null

    override fun bind(element: PlayCardViewModel) {
        element.getPlayCardHome()?.let { viewModel ->
            this.playCardHome = viewModel //flag to preventing re-hit

            bindCard(viewModel.playGetCardHome.data.card)
            container.show()

            //impression tracker
            HomePageTracking.eventEnhanceImpressionPlayBanner(view.context, element.getChannel())

            itemView.setOnClickListener {
                val appLink = viewModel.playGetCardHome.data.card.applink
                with(view.context) {
                    //event click tracker
                    HomePageTracking.eventClickPlayBanner(this, element.getChannel())

                    //start applink
                    startActivity(RouteManager.getIntent(this, appLink))
                }
            }
        }
    }

    private fun bindCard(card: PlayCard) {
        chipLive(card)
        chipViewers(card)
        ImageHandler.loadImageRounded2(view.context, imgBanner, card.imageUrl, ROUNDED_RADIUS)
    }

    private fun chipLive(card: PlayCard) {
        chipPlayLive.showWithCondition(card.isShowLive)
    }

    private fun chipViewers(card: PlayCard) {
        chipPlayViewers.showWithCondition(card.isShowTotalView)
        txtTotalViewers.text = card.totalView
    }

    companion object {
        const val ROUNDED_RADIUS = 20f
        @LayoutRes val LAYOUT = R.layout.item_home_play_card
    }

}