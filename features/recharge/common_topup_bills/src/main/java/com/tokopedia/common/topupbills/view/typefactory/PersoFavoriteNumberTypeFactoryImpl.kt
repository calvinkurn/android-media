package com.tokopedia.common.topupbills.view.typefactory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberErrorStateBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberNotFoundBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberShimmerBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsPersoFavoriteNumberBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsSavedNumberEmptyStateBinding
import com.tokopedia.common.topupbills.view.listener.PersoFavoriteNumberNotFoundStateListener
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberDataView
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberEmptyDataView
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberErrorDataView
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberNotFoundDataView
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberShimmerDataView
import com.tokopedia.common.topupbills.view.viewholder.PersoFavoriteNumberEmptyViewHolder
import com.tokopedia.common.topupbills.view.viewholder.PersoFavoriteNumberErrorViewHolder
import com.tokopedia.common.topupbills.view.viewholder.PersoFavoriteNumberNotFoundViewHolder
import com.tokopedia.common.topupbills.view.viewholder.PersoFavoriteNumberShimmerViewHolder
import com.tokopedia.common.topupbills.view.viewholder.PersoFavoriteNumberViewHolder

class PersoFavoriteNumberTypeFactoryImpl(
    private val favoriteNumberListener: PersoFavoriteNumberViewHolder.OnPersoFavoriteNumberClickListener,
    private val notFoundStateListener: PersoFavoriteNumberNotFoundStateListener,
    private val errorStateListener: PersoFavoriteNumberErrorViewHolder.PersoFavoriteNumberErrorStateListener
): PersoFavoriteNumberTypeFactory {

    override fun type(persoFavoriteNumberDataView: TopupBillsPersoFavNumberDataView): Int =
        PersoFavoriteNumberViewHolder.LAYOUT

    override fun type(emptyStateDataView: TopupBillsPersoFavNumberEmptyDataView): Int =
        PersoFavoriteNumberEmptyViewHolder.LAYOUT

    override fun type(favoriteNumberNotFoundDataView: TopupBillsPersoFavNumberNotFoundDataView): Int =
        PersoFavoriteNumberNotFoundViewHolder.LAYOUT

    override fun type(favoriteNumberShimmerDataView: TopupBillsPersoFavNumberShimmerDataView): Int =
        PersoFavoriteNumberShimmerViewHolder.LAYOUT

    override fun type(favoriteNumberErrorDataView: TopupBillsPersoFavNumberErrorDataView): Int =
        PersoFavoriteNumberErrorViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when (type) {
            PersoFavoriteNumberViewHolder.LAYOUT -> createPersoFavoriteNumberViewHolder(parent)
            PersoFavoriteNumberEmptyViewHolder.LAYOUT -> createPersoFavoriteNumberEmptyViewHolder(parent)
            PersoFavoriteNumberNotFoundViewHolder.LAYOUT -> createPersoFavoriteNumberNotFoundViewHolder(parent)
            PersoFavoriteNumberShimmerViewHolder.LAYOUT -> createPersoFavoriteNumberShimmerViewHolder(parent)
            PersoFavoriteNumberErrorViewHolder.LAYOUT -> createPersoFavoriteNumberErrorViewHolder(parent)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        }
    }

    private fun createPersoFavoriteNumberViewHolder(parent: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsPersoFavoriteNumberBinding.inflate(
            LayoutInflater.from(parent.context), parent as ViewGroup, false
        )
        return PersoFavoriteNumberViewHolder(
            binding,
            favoriteNumberListener
        ) as AbstractViewHolder<Visitable<*>>
    }

    private fun createPersoFavoriteNumberEmptyViewHolder(parent: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsSavedNumberEmptyStateBinding.inflate(
            LayoutInflater.from(parent.context), parent as ViewGroup, false
        )
        return PersoFavoriteNumberEmptyViewHolder(
            binding
        ) as AbstractViewHolder<Visitable<*>>
    }

    private fun createPersoFavoriteNumberNotFoundViewHolder(parent: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsFavoriteNumberNotFoundBinding.inflate(
            LayoutInflater.from(parent.context), parent as ViewGroup, false
        )
        return PersoFavoriteNumberNotFoundViewHolder(
            binding,
            notFoundStateListener
        ) as AbstractViewHolder<Visitable<*>>
    }

    private fun createPersoFavoriteNumberShimmerViewHolder(parent: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsFavoriteNumberShimmerBinding.inflate(
            LayoutInflater.from(parent.context), parent as ViewGroup, false
        )
        return PersoFavoriteNumberShimmerViewHolder(binding) as AbstractViewHolder<Visitable<*>>
    }

    private fun createPersoFavoriteNumberErrorViewHolder(parent: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsFavoriteNumberErrorStateBinding.inflate(
            LayoutInflater.from(parent.context), parent as ViewGroup, false
        )
        return PersoFavoriteNumberErrorViewHolder(
            binding,
            errorStateListener
        ) as AbstractViewHolder<Visitable<*>>
    }
}