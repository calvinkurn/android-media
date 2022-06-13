package com.tokopedia.common.topupbills.favorite.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberDataView
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberEmptyDataView
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberErrorDataView
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberNotFoundDataView
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberShimmerDataView

interface PersoFavoriteNumberTypeFactory {

    fun type(persoFavoriteNumberDataView: TopupBillsPersoFavNumberDataView): Int
    fun type(emptyStateDataView: TopupBillsPersoFavNumberEmptyDataView): Int
    fun type(favoriteNumberNotFoundDataView: TopupBillsPersoFavNumberNotFoundDataView): Int
    fun type(favoriteNumberShimmerDataView: TopupBillsPersoFavNumberShimmerDataView): Int
    fun type(favoriteNumberErrorDataView: TopupBillsPersoFavNumberErrorDataView): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<Visitable<*>>
}