package com.tokopedia.logisticaddaddress.features.district_recommendation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.logisticCommon.data.entity.address.Token;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress;

import java.util.ArrayList;

import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract.Constant.ARGUMENT_DATA_TOKEN;
import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract.Constant.IS_LOCALIZATION;
import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID;
import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME;
import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID;
import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME;
import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LATITUDE;
import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LONGITUDE;
import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID;
import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME;
import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES;

/**
 * Created by Irfan Khoirul on 17/11/18.
 * Deeplink: DISTRICT_RECOMMENDATION_SHOP_SETTINGS
 */

public class DiscomActivity extends BaseSimpleActivity
        implements HasComponent, DiscomFragment.ActionListener {

    private CheckoutAnalyticsChangeAddress analytics;
    private FusedLocationProviderClient fusedLocationClient;

    public static Intent newInstance(Activity activity, Token token, Boolean isLocalization) {
        Intent intent = new Intent(activity, DiscomActivity.class);
        intent.putExtra(ARGUMENT_DATA_TOKEN, token);
        intent.putExtra(IS_LOCALIZATION, isLocalization);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = new CheckoutAnalyticsChangeAddress();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            toolbar.setNavigationIcon(com.tokopedia.design.R.drawable.ic_close);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        Token  token = getIntent().getParcelableExtra(ARGUMENT_DATA_TOKEN);
        boolean isLocalization = getIntent().getBooleanExtra(IS_LOCALIZATION, false);
        if (token == null) {
            return DiscomFragment.newInstance(isLocalization);
        } else {
            return DiscomFragment.newInstance(token, isLocalization);
        }
    }

    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }

    @Override
    public void onBackPressed() {
        gtmOnBackPressClicked();
        super.onBackPressed();
    }

    @Override
    public void gtmOnBackPressClicked() {
        analytics.eventClickShippingCartChangeAddressClickXPojokKiriKotaAtauKecamatanPadaTambahAddress();
    }

    @Override
    public void gtmOnDistrictDropdownSelectionItemClicked(String districtName) {
        analytics.eventClickShippingCartChangeAddressClickChecklistKotaAtauKecamatanPadaTambahAddress(districtName);
    }

    @Override
    public void gtmOnClearTextDistrictRecommendationInput() {
        analytics.eventClickShippingCartChangeAddressClickXPojokKananKotaAtauKecamatanPadaTambahAddress();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isAllowed = false;
        for (int i=0; i<permissions.length; i++) {
            if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                isAllowed = true;
                break;
            }
        }
        if (isAllowed) {
            fusedLocationClient = new FusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this::returnResult);
        }

    }

    private void returnResult(Location location) {
        System.out.println("++ latitude = "+location.getLatitude()+", longitude = "+location.getLongitude());
        Intent resultIntent = new Intent();
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID, "");
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME, "");
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID, "");
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME, "");
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID, "");
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME, "");
        resultIntent.putStringArrayListExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES, new ArrayList<>());
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LATITUDE, location.getLatitude());
        resultIntent.putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LONGITUDE, location.getLongitude());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
