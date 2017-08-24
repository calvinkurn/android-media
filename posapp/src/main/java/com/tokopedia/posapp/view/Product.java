package com.tokopedia.posapp.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.product.listener.ViewListener;
import com.tokopedia.core.product.model.productdetail.ProductCampaign;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.posapp.view.viewmodel.product.ProductViewModel;

/**
 * Created by okasurya on 8/10/17.
 */

public interface Product {
    interface View extends CustomerView {
        void onSuccessGetProduct(ProductDetailData productDetailData);

        void onSuccessGetProductCampaign(ProductCampaign productCampaign);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getProduct(ProductPass productPass);

        void getProductCampaign(int productId);
    }
}
