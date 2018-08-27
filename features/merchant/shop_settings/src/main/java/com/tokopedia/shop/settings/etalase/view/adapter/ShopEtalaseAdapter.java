package com.tokopedia.shop.settings.etalase.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.shop.settings.etalase.data.BaseShopEtalaseViewModel;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel;
import com.tokopedia.shop.settings.etalase.view.adapter.factory.ShopEtalaseFactory;

import java.util.List;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopEtalaseAdapter extends BaseListAdapter<BaseShopEtalaseViewModel, ShopEtalaseFactory> {
    public ShopEtalaseAdapter(ShopEtalaseFactory baseListAdapterTypeFactory,
                              OnAdapterInteractionListener<BaseShopEtalaseViewModel> onAdapterInteractionListener) {
        super(baseListAdapterTypeFactory, onAdapterInteractionListener);
    }
}
