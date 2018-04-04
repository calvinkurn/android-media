package com.tokopedia.posapp.product.management.view.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author okasurya on 3/14/18.
 */

public class GetProductManagementSubsrciber extends Subscriber<List<ProductDomain>> {
    private ProductManagement.View view;

    public GetProductManagementSubsrciber(ProductManagement.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onError(e);
    }

    @Override
    public void onNext(List<ProductDomain> productList) {
        view.onReloadData(mapData(productList));
    }

    private List<Visitable> mapData(List<ProductDomain> productList) {
        List<Visitable> visitables = new ArrayList<>();
        for(ProductDomain productDomain : productList) {
            ProductViewModel productViewModel = new ProductViewModel();
            productViewModel.setId(Long.toString(productDomain.getProductId()));
            productViewModel.setName(productDomain.getProductName());
            productViewModel.setImageUrl(productDomain.getProductImage300());
            productViewModel.setOnlinePrice(productDomain.getProductOriginalPriceUnformatted());
            productViewModel.setOutletPrice(productDomain.getProductPriceUnformatted());
            visitables.add(productViewModel);
        }
        return visitables;
    }
}
