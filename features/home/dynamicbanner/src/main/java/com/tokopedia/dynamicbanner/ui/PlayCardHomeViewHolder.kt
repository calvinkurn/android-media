package com.tokopedia.dynamicbanner.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.dynamicbanner.R
import com.tokopedia.dynamicbanner.entity.PlayCard
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.android.synthetic.main.chip_home_play_live.view.*
import kotlinx.android.synthetic.main.chip_home_play_viewers.view.*
import kotlinx.android.synthetic.main.item_home_play_card.view.*

class PlayCardHomeViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val imgBanner = view.imgBanner
    private val chipPlayLive = view.chipPlayLive
    private val chipPlayViewers = view.chipPlayViewers
    private val txtTotalViewers = view.txtTotalViewers

    fun bind(card: PlayCard) {
        imgBanner.loadImageRounded(card.imageUrl)
        chipLive(card)
        chipViewers(card)
    }

    private fun chipLive(card: PlayCard) {
        chipPlayLive.showWithCondition(card.isShowLive)
    }

    private fun chipViewers(card: PlayCard) {
        chipPlayViewers.showWithCondition(card.isShowTotalView)
        txtTotalViewers.text = card.totalView.toString()
    }

    companion object {
        fun holder(context: Context, root: ViewGroup): PlayCardHomeViewHolder {
            val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_home_play_card, root, false)
            return PlayCardHomeViewHolder(view)
        }
    }

}