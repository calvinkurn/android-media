package com.tokopedia.posapp.product.productdetail.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;

/**
 * Created by okasurya on 8/10/17.
 */

public interface Product {
    interface View extends CustomerView {
        void onSuccessGetProduct(ProductDetailData productDetailData);

        void showProductCampaign();

        void navigateToActivity(Intent intent);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getProduct(ProductPass productPass);

        void processToPicturePreview(Context context, Bundle bundle);
    }
}
