package com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.FavoriteShopTypeFactory
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavVisitable
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

class EmptyStateFavoriteShopDataModel: FavoriteShopNavVisitable, ImpressHolder() {
    override fun type(factory: FavoriteShopTypeFactory): Int {
        return factory.type(this)
    }
}