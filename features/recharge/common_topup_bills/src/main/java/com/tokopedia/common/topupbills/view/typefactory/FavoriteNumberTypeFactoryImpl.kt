package com.tokopedia.common.topupbills.view.typefactory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoritNumberBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberEmptyStateBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberNotFoundBinding
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberEmptyStateListener
import com.tokopedia.common.topupbills.view.listener.OnFavoriteNumberClickListener
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberEmptyDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberNotFoundDataView
import com.tokopedia.common.topupbills.view.viewholder.FavoriteNumberEmptyViewHolder
import com.tokopedia.common.topupbills.view.viewholder.FavoriteNumberNotFoundViewHolder
import com.tokopedia.common.topupbills.view.viewholder.FavoriteNumberViewHolder
import com.tokopedia.network.exception.MessageErrorException

@Suppress("UNCHECKED_CAST")
class FavoriteNumberTypeFactoryImpl(
        private val favoriteNumberListener: OnFavoriteNumberClickListener,
        private val emptyStateListener: FavoriteNumberEmptyStateListener
): FavoriteNumberTypeFactory {

    override fun type(favoriteNumberDataView: TopupBillsFavNumberDataView): Int = FavoriteNumberViewHolder.LAYOUT

    override fun type(emptyStateDataView: TopupBillsFavNumberEmptyDataView): Int = FavoriteNumberEmptyViewHolder.LAYOUT

    override fun type(favoriteNumberNotFoundDataView: TopupBillsFavNumberNotFoundDataView): Int = FavoriteNumberNotFoundViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when(type) {
            FavoriteNumberViewHolder.LAYOUT -> createFavoriteNumberViewHolder(parent)
            FavoriteNumberEmptyViewHolder.LAYOUT -> createFavoriteNumberEmptyViewHolder(parent)
            FavoriteNumberNotFoundViewHolder.LAYOUT -> createFavoriteNumberNotFoundViewHolder(parent)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        }
    }

    private fun createFavoriteNumberViewHolder(parent: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsFavoritNumberBinding.inflate(
                LayoutInflater.from(parent.context), parent as ViewGroup, false)
        return FavoriteNumberViewHolder(binding, favoriteNumberListener) as AbstractViewHolder<Visitable<*>>
    }

    private fun createFavoriteNumberEmptyViewHolder(parent: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsFavoriteNumberEmptyStateBinding.inflate(
                LayoutInflater.from(parent.context), parent as ViewGroup, false)
        return FavoriteNumberEmptyViewHolder(binding, emptyStateListener) as AbstractViewHolder<Visitable<*>>
    }

    private fun createFavoriteNumberNotFoundViewHolder(parent: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsFavoriteNumberNotFoundBinding.inflate(
                LayoutInflater.from(parent.context), parent as ViewGroup, false)
        return FavoriteNumberNotFoundViewHolder(binding, emptyStateListener) as AbstractViewHolder<Visitable<*>>
    }
}