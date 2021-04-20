package com.tokopedia.shop.settings.notes.view.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel

/**
 * Created by hendry on 20/08/18.
 */
abstract class BaseShopNoteFactory : BaseAdapterTypeFactory() {
    abstract fun type(model: ShopNoteUiModel): Int
}
