package com.tokopedia.common.topupbills.view.typefactory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberEmptyStateBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberErrorStateBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberNotFoundBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberShimmerBinding
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberEmptyStateListener
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberErrorStateListener
import com.tokopedia.common.topupbills.view.listener.OnFavoriteNumberClickListener
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberEmptyDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberErrorDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberNotFoundDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberShimmerDataView
import com.tokopedia.common.topupbills.view.viewholder.FavoriteNumberEmptyViewHolder
import com.tokopedia.common.topupbills.view.viewholder.FavoriteNumberErrorViewHolder
import com.tokopedia.common.topupbills.view.viewholder.FavoriteNumberNotFoundViewHolder
import com.tokopedia.common.topupbills.view.viewholder.FavoriteNumberShimmerViewHolder
import com.tokopedia.common.topupbills.view.viewholder.FavoriteNumberViewHolder
import com.tokopedia.network.exception.MessageErrorException

@Suppress("UNCHECKED_CAST")
class FavoriteNumberTypeFactoryImpl(
        private val favoriteNumberListener: OnFavoriteNumberClickListener,
        private val emptyStateListener: FavoriteNumberEmptyStateListener,
        private val errorStateListener: FavoriteNumberErrorStateListener
): FavoriteNumberTypeFactory {

    override fun type(favoriteNumberDataView: TopupBillsFavNumberDataView): Int = FavoriteNumberViewHolder.LAYOUT

    override fun type(emptyStateDataView: TopupBillsFavNumberEmptyDataView): Int = FavoriteNumberEmptyViewHolder.LAYOUT

    override fun type(favoriteNumberNotFoundDataView: TopupBillsFavNumberNotFoundDataView): Int = FavoriteNumberNotFoundViewHolder.LAYOUT

    override fun type(favoriteNumberShimmerDataView: TopupBillsFavNumberShimmerDataView): Int = FavoriteNumberShimmerViewHolder.LAYOUT

    override fun type(favoriteNumberErrorDataView: TopupBillsFavNumberErrorDataView): Int = FavoriteNumberErrorViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when(type) {
            FavoriteNumberViewHolder.LAYOUT -> createFavoriteNumberViewHolder(parent)
            FavoriteNumberEmptyViewHolder.LAYOUT -> createFavoriteNumberEmptyViewHolder(parent)
            FavoriteNumberNotFoundViewHolder.LAYOUT -> createFavoriteNumberNotFoundViewHolder(parent)
            FavoriteNumberShimmerViewHolder.LAYOUT -> createFavoriteNumberShimmerViewHolder(parent)
            FavoriteNumberErrorViewHolder.LAYOUT -> createFavoriteNumberErrorViewHolder(parent)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        }
    }

    private fun createFavoriteNumberViewHolder(parent: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsFavoriteNumberBinding.inflate(
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

    private fun createFavoriteNumberShimmerViewHolder(parent: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsFavoriteNumberShimmerBinding.inflate(
                LayoutInflater.from(parent.context), parent as ViewGroup, false)
        return FavoriteNumberShimmerViewHolder(binding) as AbstractViewHolder<Visitable<*>>
    }

    private fun createFavoriteNumberErrorViewHolder(parent: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsFavoriteNumberErrorStateBinding.inflate(
                LayoutInflater.from(parent.context), parent as ViewGroup, false)
        return FavoriteNumberErrorViewHolder(binding, errorStateListener) as AbstractViewHolder<Visitable<*>>
    }
}