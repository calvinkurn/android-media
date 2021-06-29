package com.tokopedia.common.topupbills.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberEmptyDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberNotFoundDataView

interface FavoriteNumberTypeFactory {

    fun type(favoriteNumberDataView: TopupBillsFavNumberDataView): Int
    fun type(emptyStateDataView: TopupBillsFavNumberEmptyDataView): Int
    fun type(favoriteNumberNotFoundDataView: TopupBillsFavNumberNotFoundDataView): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<Visitable<*>>
}