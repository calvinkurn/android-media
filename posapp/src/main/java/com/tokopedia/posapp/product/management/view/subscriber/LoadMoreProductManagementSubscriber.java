package com.tokopedia.posapp.product.management.view.subscriber;

import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.management.view.ProductManagement;

import java.util.List;

import javax.inject.Inject;

/**
 * @author okasurya on 4/8/18.
 */

public class LoadMoreProductManagementSubscriber extends GetProductManagementSubscriber {
    @Inject
    public LoadMoreProductManagementSubscriber(ProductManagement.View view) {
        super(view);
    }

    @Override
    public void onNext(List<ProductDomain> productList) {
        view.onLoadMore(mapData(productList));
    }
}
