package com.tokopedia.transactionanalytics.listener;

/**
 * @author anggaprasetiyo on 20/07/18.
 */
public interface ITransactionAnalyticsGeoLocationPinPoint {

    void sendAnalyticsOnDropdownSuggestionItemClicked();

    void sendAnalyticsOnSetCurrentMarkerAsCurrentPosition();

    void sendAnalyticsOnBackPressClicked();

    void sendAnalyticsOnGetCurrentLocationClicked();

    void sendAnalyticsOnViewErrorSetPinPointLocation(String errorMessage);

}
