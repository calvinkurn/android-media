package com.tokopedia.district_recommendation.view.shopsettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.district_recommendation.view.AddressViewModel;
import com.tokopedia.district_recommendation.view.DistrictRecommendationFragment;

import static com.tokopedia.district_recommendation.view.DistrictRecommendationContract.Constant.ARGUMENT_DATA_TOKEN;

/**
 * temporary implementation to avoid core module
 */
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
    public void onItemClicked(AddressViewModel addressViewModel) {
        if (getActivity() != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID, addressViewModel.getAddress().getDistrictId());
            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME, addressViewModel.getAddress().getDistrictName());
            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID, addressViewModel.getAddress().getCityId());
            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME, addressViewModel.getAddress().getCityName());
            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID, addressViewModel.getAddress().getProvinceId());
            resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME, addressViewModel.getAddress().getProvinceName());
            resultIntent.putStringArrayListExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES, addressViewModel.getAddress().getZipCodes());
            getActivity().setResult(Activity.RESULT_OK, resultIntent);
            getActivity().finish();
        }
    }

}
