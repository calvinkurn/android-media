package com.tokopedia.shop.info.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.shop.info.view.model.ShopInfoLogisticViewModel;

public interface ShopInfoLogisticTypeFactory extends AdapterTypeFactory {

    int type(ShopInfoLogisticViewModel viewModel);
}
