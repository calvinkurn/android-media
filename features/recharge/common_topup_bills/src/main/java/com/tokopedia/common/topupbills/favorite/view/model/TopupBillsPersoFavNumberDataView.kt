package com.tokopedia.common.topupbills.favorite.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.favorite.view.typefactory.PersoFavoriteNumberTypeFactory

class TopupBillsPersoFavNumberDataView(
    val title: String,
    val subtitle: String,
    val iconUrl: String,
    val categoryId: String,
    val operatorId: String,
    val productId: String,
    val operatorName: String
): Visitable<PersoFavoriteNumberTypeFactory> {

    override fun type(typeFactory: PersoFavoriteNumberTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getClientName(): String {
        return if (subtitle.isNotEmpty())
            title else ""
    }

    fun getClientNumber(): String {
        return if (subtitle.isNotEmpty())
            subtitle else title
    }
}