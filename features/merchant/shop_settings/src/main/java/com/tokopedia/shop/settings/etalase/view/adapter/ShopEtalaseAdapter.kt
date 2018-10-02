package com.tokopedia.shop.settings.etalase.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.settings.etalase.data.BaseShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.view.adapter.factory.ShopEtalaseFactory

/**
 * Created by hendry on 16/08/18.
 */
class ShopEtalaseAdapter(baseListAdapterTypeFactory: ShopEtalaseFactory,
                         onAdapterInteractionListener: BaseListAdapter.OnAdapterInteractionListener<BaseShopEtalaseViewModel>)
    : BaseListAdapter<BaseShopEtalaseViewModel, ShopEtalaseFactory>(baseListAdapterTypeFactory, onAdapterInteractionListener)
