package com.tokopedia.digital.product.presenter;

import android.net.Uri;

import com.tokopedia.digital.product.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.model.ContactData;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalPresenter {
    String TAG = IProductDigitalPresenter.class.getSimpleName();

    void processGetCategoryAndBannerData();

    ContactData processGenerateContactDataFromUri(Uri contactURI);

    void processStateDataToReRender();

    void processAddToCartProduct(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct);
}
