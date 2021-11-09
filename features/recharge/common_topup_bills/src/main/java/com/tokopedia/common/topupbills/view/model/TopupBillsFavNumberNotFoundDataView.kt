package com.tokopedia.common.topupbills.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.view.typefactory.FavoriteNumberTypeFactory

class TopupBillsFavNumberNotFoundDataView: Visitable<FavoriteNumberTypeFactory> {
    override fun type(typeFactory: FavoriteNumberTypeFactory): Int {
        return typeFactory.type(this)
    }
}