package com.tokopedia.posapp.view.subscriber;

import com.tokopedia.core.product.model.productdetail.ProductCampaign;
import com.tokopedia.posapp.view.Product;

import rx.Subscriber;

/**
 * Created by okasurya on 8/11/17.
 */

public class GetProductCampaignSubscriber extends Subscriber<ProductCampaign> {
    Product.View viewListener;

    public GetProductCampaignSubscriber(Product.View view) {
        this.viewListener = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(ProductCampaign productCampaign) {
        viewListener.onSuccessGetProductCampaign(productCampaign);
    }
}
