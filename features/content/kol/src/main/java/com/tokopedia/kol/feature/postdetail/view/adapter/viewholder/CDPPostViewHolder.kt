package com.tokopedia.kol.feature.postdetail.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kol.feature.postdetail.view.adapter.CDPRevampAdapter


class CDPPostViewHolder(
    itemView: View,
    private val dataSource: CDPRevampAdapter.DataSource,
    private val cdpListener: CDPListener
) : BaseViewHolder(itemView) {

    companion object {

        fun create(
            parent: ViewGroup,
            dataSource: CDPRevampAdapter.DataSource,
            cdpListener: CDPListener
        ) = CDPPostViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    com.tokopedia.feedcomponent.R.layout.item_dynamic_post_new,
                    parent,
                    false,
                ),
            dataSource,
            cdpListener
        )
    }
    fun bind() {
        val card = dataSource.getData()
    }
    interface CDPListener {

    }
}