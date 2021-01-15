package com.tokopedia.shop.info.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.shop.info.view.model.ShopInfoLogisticUiModel;

public interface ShopInfoLogisticTypeFactory extends AdapterTypeFactory {

    int type(ShopInfoLogisticUiModel viewModel);
}
