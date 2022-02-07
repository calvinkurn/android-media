package com.tokopedia.play.broadcaster.setup.product.view.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.EtalaseListViewHolder

/**
 * Created by kenny.hadisaputra on 27/01/22
 */
internal class EtalaseListAdapterDelegate private constructor() {

    internal class Header : TypedAdapterDelegate<
            EtalaseListAdapter.Model.Header,
            EtalaseListAdapter.Model,
            EtalaseListViewHolder.Header>(R.layout.view_empty) {

        override fun onBindViewHolder(
            item: EtalaseListAdapter.Model.Header,
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
            EtalaseListAdapter.Model.Campaign,
            EtalaseListAdapter.Model,
            EtalaseListViewHolder.Body>(R.layout.view_empty) {

        override fun onBindViewHolder(
            item: EtalaseListAdapter.Model.Campaign,
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
            EtalaseListAdapter.Model.Etalase,
            EtalaseListAdapter.Model,
            EtalaseListViewHolder.Body>(R.layout.view_empty) {

        override fun onBindViewHolder(
            item: EtalaseListAdapter.Model.Etalase,
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