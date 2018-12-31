package com.tokopedia.digital.product.view.presenter;

import com.tokopedia.common_digital.product.presentation.model.Operator;

import java.util.List;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public interface IProductDigitalPresenter {

    String TAG = IProductDigitalPresenter.class.getSimpleName();

    void processGetCategoryAndBannerData(
            String categoryId, String operatorId, String productId, String clientNumber
    );

    void getCategoryData(String categoryId, String operatorId, String productId, String clientNumber);

    void processStateDataToReRender();

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

    void processGetHelpUrlData(String categoryId);

    void onHelpMenuClicked();

}
