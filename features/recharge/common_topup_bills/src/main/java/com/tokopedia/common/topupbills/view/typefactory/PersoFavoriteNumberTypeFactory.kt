package com.tokopedia.common.topupbills.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberDataView
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberEmptyDataView
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberErrorDataView
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberNotFoundDataView
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberShimmerDataView

interface PersoFavoriteNumberTypeFactory {

    fun type(persoFavoriteNumberDataView: TopupBillsPersoFavNumberDataView): Int
    fun type(emptyStateDataView: TopupBillsPersoFavNumberEmptyDataView): Int
    fun type(favoriteNumberNotFoundDataView: TopupBillsPersoFavNumberNotFoundDataView): Int
    fun type(favoriteNumberShimmerDataView: TopupBillsPersoFavNumberShimmerDataView): Int
    fun type(favoriteNumberErrorDataView: TopupBillsPersoFavNumberErrorDataView): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<Visitable<*>>
}