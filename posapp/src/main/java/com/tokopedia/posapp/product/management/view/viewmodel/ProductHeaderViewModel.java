package com.tokopedia.posapp.product.management.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.posapp.product.management.view.adapter.ProductManagementAdapterTypeFactory;

/**
 * @author okasurya on 3/13/18.
 */

public class ProductHeaderViewModel implements Visitable<ProductManagementAdapterTypeFactory> {
    @Override
    public int type(ProductManagementAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
