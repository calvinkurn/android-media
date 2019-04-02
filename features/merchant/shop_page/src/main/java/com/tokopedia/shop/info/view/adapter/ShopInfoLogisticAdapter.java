package com.tokopedia.shop.info.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;

import java.util.List;

/**
 * Created by nathan on 2/12/18.
 */

public class ShopInfoLogisticAdapter extends BaseAdapter<ShopInfoLogisticTypeFactory> {

    public ShopInfoLogisticAdapter(ShopInfoLogisticTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }
}
