package com.tokopedia.digital.tokocash.topup;

import android.app.Application;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.model.CategoryData;

/**
 * Created by nabillasabbaha on 2/28/18.
 */

public interface TopupTokoCashContract {

    interface View extends CustomerView {
        void renderProductTokoCash(CategoryData categoryData);

        void showErrorLoadProductTokoCash(Throwable throwable);

        Application getMainApplication();

        void navigateToActivityRequest(Intent intent, int requestCode);

        String getVersionInfoApplication();

        String getUserLoginId();
    }

    interface Presenter extends CustomerPresenter<View> {
        void processGetDataProductTokoCash();

        void processAddToCartProduct(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct);

        void destroyView();
    }
}
