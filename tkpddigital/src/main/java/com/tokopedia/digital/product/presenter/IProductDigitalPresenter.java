package com.tokopedia.digital.product.presenter;

import android.net.Uri;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.product.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.model.ContactData;
import com.tokopedia.digital.product.model.Operator;

import java.util.List;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalPresenter {
    String TAG = IProductDigitalPresenter.class.getSimpleName();

    void processGetCategoryAndBannerData(
            String categoryId, String operatorId, String productId, String clientNumber
    );

    void processStoreLastInputClientNumberByCategory(
            String lastClientNumber, String categoryId, String operatorId, String productId
    );

    ContactData processGenerateContactDataFromUri(Uri contactURI);

    void processStateDataToReRender();

    void processAddToCartProduct(DigitalCheckoutPassData digitalCheckoutPassData);

    DigitalCheckoutPassData generateCheckoutPassData(
            BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct
    );

    void processToCheckBalance(String ussdMobileNumber, int simSlot, String ussdCode);

    void processPulsaBalanceUssdResponse(String result,int selectedSim);

    String getDeviceMobileNumber(int selectedSim);

    List<Operator> getSelectedUssdOperatorList(int selectedSim);

    void removeUssdTimerCallback();

    String getUssdPhoneNumberFromCache(int selectedSim);

    void storeUssdPhoneNumber(int selectedSim,String number);

    Operator getSelectedUssdOperator(int selectedSim);

    boolean isCarrierSignalsNotAvailable(String carrierName);

    void renderCheckPulsa();

}
