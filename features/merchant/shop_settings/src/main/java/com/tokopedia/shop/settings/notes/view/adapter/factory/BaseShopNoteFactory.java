package com.tokopedia.shop.settings.notes.view.adapter.factory;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;

/**
 * Created by hendry on 20/08/18.
 */
public abstract class BaseShopNoteFactory extends BaseAdapterTypeFactory {
    public abstract int type(ShopNoteViewModel model);
}
