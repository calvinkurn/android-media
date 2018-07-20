package com.tokopedia.transactionanalytics.listener;

/**
 * @author anggaprasetiyo on 19/07/18.
 */
public interface ITransactionAnalyticsAddAddress {

    void sendAnalyticsOnSubmitSaveAddressClicked();

    void sendAnalyticsOnDistrictSelectionClicked();

    void sendAnalyticsOnZipCodeSelectionClicked();

    void sendAnalyticsOnLocationSelectionClicked();

    void sendAnalyticsOnZipCodeDropdownSelectionClicked();

    void sendAnalyticsOnZipCodeInputFreeText(String zipCode);

    void sendAnalyticsOnValidationErrorSaveAddress(String errorMessageValidation);

}
