package com.tokopedia.common.topupbills.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.view.model.FavoriteNumberDataView
import com.tokopedia.common.topupbills.view.model.FavoriteNumberEmptyDataView
import com.tokopedia.common.topupbills.view.model.FavoriteNumberNotFoundDataView

interface FavoriteNumberTypeFactory {

    fun type(favoriteNumberDataView: FavoriteNumberDataView): Int
    fun type(emptyStateDataView: FavoriteNumberEmptyDataView): Int
    fun type(favoriteNumberNotFoundDataView: FavoriteNumberNotFoundDataView): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<Visitable<*>>
}