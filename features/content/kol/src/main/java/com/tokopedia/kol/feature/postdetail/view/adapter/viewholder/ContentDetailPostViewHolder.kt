package com.tokopedia.kol.feature.postdetail.view.adapter.viewholder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.postdetail.view.adapter.ContentDetailPageRevampAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.user.session.UserSession


class ContentDetailPostViewHolder(
    itemView: View,
    private val dataSource: ContentDetailPageRevampAdapter.DataSource,
    private val cdpListener: CDPListener
) : BaseViewHolder(itemView) {

    private val cdpView =
        itemView.findViewById<ContentDetailPostTypeViewHolder>(R.id.item_cdp_revamp_view_item)


    companion object {

        fun create(
            parent: ViewGroup,
            dataSource: ContentDetailPageRevampAdapter.DataSource,
            cdpListener: CDPListener
        ) = ContentDetailPostViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_cdp_revamp_view,
                    parent,
                    false,
                ),
            dataSource,
            cdpListener
        )
    }
    fun bind(feedXCard: FeedXCard) {
        cdpView.bindData(cdpListener, adapterPosition, feedXCard, UserSession(itemView.context))
    }
    fun bindWithPayloads(feedXCard: FeedXCard, payloads: Bundle) {
        if (feedXCard == null) {
            itemView.hide()
            return
        }
        if (feedXCard.media.size > feedXCard.lastCarouselIndex) {
            val media = feedXCard.media[feedXCard.lastCarouselIndex]
            if (feedXCard.isTypeVOD|| feedXCard.isTypeLongVideo)
                cdpView.playVOD(feedXCard)
            else if (media.isVideo || media.isImage)
                cdpView.bindImageOnImpress()
        }
    }
    interface CDPListener {

    }

}