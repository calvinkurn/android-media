package com.tokopedia.digital.common.view.presenter;

import android.content.ContentResolver;
import android.net.Uri;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.model.ContactData;

/**
 * Created by nabillasabbaha on 8/8/17.
 * Modified by rizkyfadillah at 10/6/17.
 */

public interface IBaseDigitalPresenter {

    void storeLastClientNumberTyped(String categoryId, String operatorId, String clientNumber,
                                    String productId);

    String getLastOperatorSelected(String categoryId);

    String getLastClientNumberTyped(String categoryId);

    String getLastProductSelected(String categoryId);

    ContactData processGenerateContactDataFromUri(Uri contactURI, ContentResolver contentResolver);

    DigitalCheckoutPassData generateCheckoutPassData(
            BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct,
            String versionInfoApplication,
            String userLoginId
    );

    void detachView();

}
