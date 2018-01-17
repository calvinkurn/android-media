package com.tokopedia.digital.widget.presenter;

import android.content.ContentResolver;
import android.net.Uri;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.product.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.model.ContactData;

/**
 * Created by Rizky on 15/01/18.
 */

public interface IDigitalWidgetPresenter {

    void fetchCategory(String categoryId);

    DigitalCheckoutPassData generateCheckoutPassData(
            BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct,
            String versionInfoApplication,
            String userLoginId
    );

    ContactData processGenerateContactDataFromUri(Uri contactURI, ContentResolver contentResolver);

    void processStoreLastInputClientNumberByCategory(
            String lastClientNumber, String categoryId, String operatorId, String productId,
            LocalCacheHandler cacheHandlerLastInputClientNumber
    );

}
