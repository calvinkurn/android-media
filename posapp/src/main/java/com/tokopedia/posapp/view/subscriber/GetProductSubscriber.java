package com.tokopedia.posapp.view.subscriber;

import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.domain.model.product.ProductDomain;
import com.tokopedia.posapp.view.Product;
import com.tokopedia.posapp.view.viewmodel.product.ProductViewModel;

import rx.Subscriber;

/**
 * Created by okasurya on 8/10/17.
 */

public class GetProductSubscriber extends Subscriber<ProductDetailData> {
    Product.View viewListener;

    public GetProductSubscriber(Product.View view) {
        this.viewListener = view;
    }


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(ProductDetailData productDetailData) {
        viewListener.onSuccessGetProduct(productDetailData);
    }
}
