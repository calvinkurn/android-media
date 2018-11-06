package com.tokopedia.district_recommendation.view.shopsettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.district_recommendation.domain.model.Address;
import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.district_recommendation.view.DistrictRecommendationFragment;

import static com.tokopedia.district_recommendation.view.DistrictRecommendationContract.Constant.ARGUMENT_DATA_TOKEN;

/** temporary implementation to avoid core module */
public class DistrictRecommendationShopSettingsFragment extends DistrictRecommendationFragment {
    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID = "district_recommendation_address_district_id";
    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME = "district_recommendation_address_district_name";
    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID = "district_recommendation_address_city_id";
    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME = "district_recommendation_address_city_name";
    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID = "district_recommendation_address_province_id";
    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME = "district_recommendation_address_province_name";
    private static final String INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES = "district_recommendation_address_zipcodes";

    public static DistrictRecommendationShopSettingsFragment newInstance(Token token) {
        DistrictRecommendationShopSettingsFragment fragment = new DistrictRecommendationShopSettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_DATA_TOKEN, token);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onItemClick(Address address) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID, address.getDistrictId());
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME, address.getDistrictName());
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID, address.getCityId());
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME, address.getCityName());
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID, address.getProvinceId());
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME, address.getProvinceName());
        resultIntent.putStringArrayListExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES, address.getZipCodes());
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
        getActivity().finish();
    }
}
