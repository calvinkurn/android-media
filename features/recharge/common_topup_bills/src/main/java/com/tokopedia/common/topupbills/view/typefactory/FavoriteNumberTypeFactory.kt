package com.tokopedia.common.topupbills.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberDataView
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberEmptyDataView
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberErrorDataView
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberNotFoundDataView
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberShimmerDataView

interface FavoriteNumberTypeFactory {

    fun type(favoriteNumberDataView: TopupBillsFavNumberDataView): Int
    fun type(emptyStateDataView: TopupBillsFavNumberEmptyDataView): Int
    fun type(favoriteNumberNotFoundDataView: TopupBillsFavNumberNotFoundDataView): Int
    fun type(favoriteNumberShimmerDataView: TopupBillsFavNumberShimmerDataView): Int
    fun type(favoriteNumberErrorDataView: TopupBillsFavNumberErrorDataView): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<Visitable<*>>
}