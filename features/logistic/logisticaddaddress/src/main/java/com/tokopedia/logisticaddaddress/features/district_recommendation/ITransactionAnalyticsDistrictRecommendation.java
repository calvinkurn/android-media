package com.tokopedia.logisticaddaddress.features.district_recommendation;

/**
 * @author anggaprasetiyo on 20/07/18.
 */
public interface ITransactionAnalyticsDistrictRecommendation {
    void sendAnalyticsOnBackPressClicked();

    void sendAnalyticsOnDistrictDropdownSelectionItemClicked(String districtName);

    void sendAnalyticsOnClearTextDistrictRecommendationInput();
}
