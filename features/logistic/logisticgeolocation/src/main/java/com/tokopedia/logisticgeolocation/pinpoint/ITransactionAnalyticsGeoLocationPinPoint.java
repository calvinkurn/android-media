package com.tokopedia.logisticgeolocation.pinpoint;

/**
 * @author anggaprasetiyo on 20/07/18.
 * Refactored by fajarnuha
 */
public interface ITransactionAnalyticsGeoLocationPinPoint {

    void sendAnalyticsOnDropdownSuggestionItemClicked();

    void sendAnalyticsOnSetCurrentMarkerAsCurrentPosition();

    void sendAnalyticsOnBackPressClicked();

    void sendAnalyticsOnGetCurrentLocationClicked();

    void sendAnalyticsOnViewErrorSetPinPointLocation(String errorMessage);

}

