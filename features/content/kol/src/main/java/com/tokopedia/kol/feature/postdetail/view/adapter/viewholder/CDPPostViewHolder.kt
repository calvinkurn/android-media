package com.tokopedia.kol.feature.postdetail.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.postdetail.view.adapter.CDPRevampAdapter
import com.tokopedia.user.session.UserSession


class CDPPostViewHolder(
    itemView: View,
    private val dataSource: CDPRevampAdapter.DataSource,
    private val cdpListener: CDPListener
) : BaseViewHolder(itemView) {

    private val cdpView =
        itemView.findViewById<CDPPostContentTypeViewHolder>(R.id.item_cdp_revamp_view_item)


    companion object {

        fun create(
            parent: ViewGroup,
            dataSource: CDPRevampAdapter.DataSource,
            cdpListener: CDPListener
        ) = CDPPostViewHolder(
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
    interface CDPListener {

    }
}