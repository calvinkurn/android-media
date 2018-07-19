package com.tokopedia.posapp.product.management.view.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author okasurya on 3/14/18.
 */

public class GetProductManagementSubscriber extends Subscriber<List<ProductDomain>> {
    protected ProductManagement.View view;

    public GetProductManagementSubscriber(ProductManagement.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onErrorLoadData(e);
    }

    @Override
    public void onNext(List<ProductDomain> productList) {
        view.onReloadData(mapData(productList));
    }

    protected List<Visitable> mapData(List<ProductDomain> productList) {
        List<Visitable> visitables = new ArrayList<>();
        for(ProductDomain productDomain : productList) {
            ProductViewModel productViewModel = new ProductViewModel();
            productViewModel.setId(Long.toString(productDomain.getId()));
            productViewModel.setName(productDomain.getName());
            productViewModel.setImageUrl(productDomain.getImage300());
            productViewModel.setOnlinePriceUnformatted(productDomain.getOriginalPriceUnformatted());
            productViewModel.setOutletPriceUnformatted(productDomain.getPriceUnformatted());
            productViewModel.setOnlinePrice(productDomain.getOriginalPrice());
            productViewModel.setOutletPrice(productDomain.getPrice());
            productViewModel.setEtalaseId(productDomain.getEtalaseId());
            productViewModel.setStatus(productDomain.getStatus());
            visitables.add(productViewModel);
        }
        return visitables;
    }
}
