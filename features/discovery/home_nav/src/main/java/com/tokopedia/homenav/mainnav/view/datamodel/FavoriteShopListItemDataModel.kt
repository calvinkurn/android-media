package com.tokopedia.homenav.mainnav.view.datamodel

import com.tokopedia.homenav.mainnav.domain.model.NavOrderListModel
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class FavoriteShopListItemDataModel(
        val orderListModel: NavOrderListModel,
        val othersTransactionCount: Int = 0
): MainNavVisitable, ImpressHolder() {
    override fun id(): Any = "oderList"

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean =
            visitable is FavoriteShopListItemDataModel &&
                    visitable.orderListModel == orderListModel

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}