package com.tokopedia.transactionanalytics.listener;

/**
 * @author anggaprasetiyo on 19/07/18.
 */
public interface ITransactionAnalyticsAddAddress {

    void sendAnalyticsOnDistrictSelectionClicked();

    void sendAnalyticsOnZipCodeSelectionClicked();

    void sendAnalyticsOnLocationSelectionClicked();

    void sendAnalyticsOnZipCodeDropdownSelectionClicked();

    void sendAnalyticsOnZipCodeInputFreeText(String zipCode);

    void sendAnalyticsOnValidationErrorSaveAddress(String errorMessageValidation);

    void sendAnalyticsOnInputAddressAsClicked();

    void sendAnalyticsOnInputAddressAsDropdownSelectionItemCliked();

    void sendAnalyticsOnInputNameClicked();

    void sendAnalyticsOnInputPhoneClicked();

    void sendAnalyticsOnInputAddressClicked();


    void sendAnalyticsOnErrorInputAddressAs();

    void sendAnalyticsOnErrorInputName();

    void sendAnalyticsOnErrorInputPhone();

    void sendAnalyticsOnErrorInputDistrict();

    void sendAnalyticsOnErrorInputZipCode();

    void sendAnalyticsOnErrorInputAddress();

    void sendAnalyticsOnSaveAddressButtonWithoutErrorValidation(boolean success);

    void sendAnalyticsScreenName(String screenName);


}
