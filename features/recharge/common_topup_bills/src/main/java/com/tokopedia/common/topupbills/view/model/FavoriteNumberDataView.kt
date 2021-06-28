package com.tokopedia.common.topupbills.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.view.typefactory.FavoriteNumberTypeFactory

class FavoriteNumberDataView(
        val favoriteNumber: TopupBillsSeamlessFavNumberItem
): Visitable<FavoriteNumberTypeFactory> {

    override fun type(typeFactory: FavoriteNumberTypeFactory): Int {
        return typeFactory.type(this)
    }
}