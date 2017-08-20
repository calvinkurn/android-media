package com.tokopedia.digital.product.presenter;

import android.net.Uri;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.product.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.model.ContactData;
import com.tokopedia.digital.product.model.Operator;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalPresenter {
    String TAG = IProductDigitalPresenter.class.getSimpleName();

    void processGetCategoryAndBannerData();

    void processStoreLastInputClientNumberByCategory(
            String lastClientNumber, String categoryId, String operatorId, String productId
    );

    ContactData processGenerateContactDataFromUri(Uri contactURI);

    void processStateDataToReRender();

    void processAddToCartProduct(DigitalCheckoutPassData digitalCheckoutPassData);

    DigitalCheckoutPassData generateCheckoutPassData(
            BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct
    );

    void processToCheckBalance(String ussdMobileNumber,int simPosition,String ussdCode);

    void processPulsaBalanceUssdResponse(String result,int selectedSim);

    String getCurrentMobileNumber(int simPosition);

    Operator getSelectedUssdOperator(int simPosition);
}
