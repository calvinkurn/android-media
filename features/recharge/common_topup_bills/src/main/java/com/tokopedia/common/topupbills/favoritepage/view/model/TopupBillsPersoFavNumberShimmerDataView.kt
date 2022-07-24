package com.tokopedia.common.topupbills.favoritepage.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.favoritepage.view.typefactory.PersoFavoriteNumberTypeFactory

class TopupBillsPersoFavNumberShimmerDataView: Visitable<PersoFavoriteNumberTypeFactory> {
    override fun type(typeFactory: PersoFavoriteNumberTypeFactory): Int {
        return typeFactory.type(this)
    }
}