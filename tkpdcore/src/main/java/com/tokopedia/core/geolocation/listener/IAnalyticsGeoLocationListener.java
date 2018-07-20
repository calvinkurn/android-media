package com.tokopedia.core.geolocation.listener;

/**
 * @author anggaprasetiyo on 20/07/18.
 */
public interface IAnalyticsGeoLocationListener {

    void sendAnalyticsOnDropdownSuggestionItemClicked();

    void sendAnalyticsOnSetCurrentMarkerAsCurrentPosition();

    void sendAnalyticsOnBackPressClicked();

    void sendAnalyticsOnGetCurrentLocationClicked();

    void sendAnalyticsOnViewErrorSetPinPointLocation(String errorMessage);

}
