package com.tokopedia.core.manage.people.address.listener;

/**
 * @author anggaprasetiyo on 19/07/18.
 */
public interface IAnalyticsAddAddressFragmentListener {

    void sendAnalyticsOnSubmitSaveAddressClicked();

    void sendAnalyticsOnDistrictSelectionClicked();

    void sendAnalyticsOnZipCodeSelectionClicked();

    void sendAnalyticsOnLocationSelectionClicked();

    void sendAnalyticsOnZipCodeDropdownSelectionClicked();

    void sendAnalyticsOnZipCodeInputFreeText(String zipCode);

    void sendAnalyticsOnValidationErrorSaveAddress(String errorMessageValidation);

}
