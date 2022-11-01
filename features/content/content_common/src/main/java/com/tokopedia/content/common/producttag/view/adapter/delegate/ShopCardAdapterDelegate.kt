package com.tokopedia.content.common.producttag.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.R
import com.tokopedia.content.common.producttag.view.adapter.ShopCardAdapter
import com.tokopedia.content.common.producttag.view.adapter.viewholder.LoadingViewHolder
import com.tokopedia.content.common.producttag.view.adapter.viewholder.ShopCardViewHolder
import com.tokopedia.content.common.producttag.view.uimodel.ShopUiModel

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
internal class ShopCardAdapterDelegate private constructor() {

    internal class Shop(
        private val onSelected: (ShopUiModel, Int) -> Unit,
    ) : TypedAdapterDelegate<
            ShopCardAdapter.Model.Shop,
            ShopCardAdapter.Model,
            ShopCardViewHolder.Shop>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: ShopCardAdapter.Model.Shop,
            holder: ShopCardViewHolder.Shop
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ShopCardViewHolder.Shop {
            return ShopCardViewHolder.Shop.create(
                parent, onSelected
            )
        }
    }

    internal class Loading : TypedAdapterDelegate<
            ShopCardAdapter.Model.Loading,
            ShopCardAdapter.Model,
            LoadingViewHolder>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: ShopCardAdapter.Model.Loading,
            holder: LoadingViewHolder
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): LoadingViewHolder {
            return LoadingViewHolder.create(parent)
        }
    }

    internal class EmptyState: TypedAdapterDelegate<
            ShopCardAdapter.Model.EmptyState,
            ShopCardAdapter.Model,
            ShopCardViewHolder.EmptyState>(
        R.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: ShopCardAdapter.Model.EmptyState,
            holder: ShopCardViewHolder.EmptyState
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ShopCardViewHolder.EmptyState {
            return ShopCardViewHolder.EmptyState.create(
                parent
            )
        }
    }
}