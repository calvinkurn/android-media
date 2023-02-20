package com.tokopedia.shop.search.view.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.search.view.adapter.ShopSearchProductAdapterTypeFactory

abstract class ShopSearchProductDataModel : Visitable<ShopSearchProductAdapterTypeFactory> {

    abstract var type: Type

    enum class Type {
        TYPE_PDP, TYPE_SEARCH_STORE
    }
}
