package com.tokopedia.posapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
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
