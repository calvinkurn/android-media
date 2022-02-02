package com.tokopedia.play.broadcaster.setup.product.view.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.setup.product.view.model.EtalaseListModel
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.EtalaseListViewHolder
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel

/**
 * Created by kenny.hadisaputra on 27/01/22
 */
internal class EtalaseListAdapterDelegate private constructor() {

    internal class Header : TypedAdapterDelegate<
            EtalaseListModel.Header,
            EtalaseListModel,
            EtalaseListViewHolder.Header>(R.layout.view_empty) {

        override fun onBindViewHolder(
            item: EtalaseListModel.Header,
            holder: EtalaseListViewHolder.Header
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): EtalaseListViewHolder.Header {
            return EtalaseListViewHolder.Header.create(parent)
        }
    }

    internal class Campaign(
        private val listener: EtalaseListViewHolder.Body.Listener,
    ) : TypedAdapterDelegate<
            EtalaseListModel.Campaign,
            EtalaseListModel,
            EtalaseListViewHolder.Body>(R.layout.view_empty) {

        override fun onBindViewHolder(
            item: EtalaseListModel.Campaign,
            holder: EtalaseListViewHolder.Body
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): EtalaseListViewHolder.Body {
            return EtalaseListViewHolder.Body.create(parent, listener)
        }
    }

    internal class Etalase(
        private val listener: EtalaseListViewHolder.Body.Listener,
    ) : TypedAdapterDelegate<
            EtalaseListModel.Etalase,
            EtalaseListModel,
            EtalaseListViewHolder.Body>(R.layout.view_empty) {

        override fun onBindViewHolder(
            item: EtalaseListModel.Etalase,
            holder: EtalaseListViewHolder.Body
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): EtalaseListViewHolder.Body {
            return EtalaseListViewHolder.Body.create(parent, listener)
        }
    }
}