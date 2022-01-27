package com.tokopedia.play.broadcaster.setup.product.view.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.setup.product.view.model.EtalaseListModel
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.PlayBroEtalaseListViewHolder

/**
 * Created by kenny.hadisaputra on 27/01/22
 */
internal class PlayBroEtalaseListAdapterDelegate private constructor() {

    internal class Header : TypedAdapterDelegate<
            EtalaseListModel.Header,
            EtalaseListModel,
            PlayBroEtalaseListViewHolder.Header>(R.layout.view_empty) {

        override fun onBindViewHolder(
            item: EtalaseListModel.Header,
            holder: PlayBroEtalaseListViewHolder.Header
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayBroEtalaseListViewHolder.Header {
            return PlayBroEtalaseListViewHolder.Header.create(parent)
        }
    }

    internal class Body : TypedAdapterDelegate<
            EtalaseListModel.Body,
            EtalaseListModel,
            PlayBroEtalaseListViewHolder.Body>(R.layout.view_empty) {

        override fun onBindViewHolder(
            item: EtalaseListModel.Body,
            holder: PlayBroEtalaseListViewHolder.Body
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayBroEtalaseListViewHolder.Body {
            return PlayBroEtalaseListViewHolder.Body.create(parent)
        }
    }
}