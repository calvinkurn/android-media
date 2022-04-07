package com.tokopedia.common.topupbills.favorite.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.favorite.view.typefactory.PersoFavoriteNumberTypeFactory

class TopupBillsPersoFavNumberEmptyDataView: Visitable<PersoFavoriteNumberTypeFactory> {
    override fun type(typeFactory: PersoFavoriteNumberTypeFactory): Int {
        return typeFactory.type(this)
    }
}