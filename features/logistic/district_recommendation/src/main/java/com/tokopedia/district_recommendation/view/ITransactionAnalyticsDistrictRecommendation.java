package com.tokopedia.district_recommendation.view;

/**
 * @author anggaprasetiyo on 20/07/18.
 */
public interface ITransactionAnalyticsDistrictRecommendation {
    void sendAnalyticsOnBackPressClicked();

    void sendAnalyticsOnDistrictDropdownSelectionItemClicked(String districtName);

    void sendAnalyticsOnClearTextDistrictRecommendationInput();
}
