package com.tokopedia.logisticaddaddress.features.district_recommendation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.localizationchooseaddress.analytics.ChooseAddressTracking;
import com.tokopedia.logisticCommon.data.entity.address.Token;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract.Constant.ARGUMENT_DATA_TOKEN;
import static com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract.Constant.IS_LOCALIZATION;

/**
 * Created by Irfan Khoirul on 17/11/18.
 * Deeplink: DISTRICT_RECOMMENDATION_SHOP_SETTINGS
 */

public class DiscomActivity extends BaseSimpleActivity
        implements HasComponent, DiscomFragment.ActionListener {

    private CheckoutAnalyticsChangeAddress analytics;
    private FusedLocationProviderClient fusedLocationClient;
    private Boolean isLocalization;

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
            toolbar.setNavigationIcon(com.tokopedia.design.R.drawable.ic_close_thin);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        Token  token = getIntent().getParcelableExtra(ARGUMENT_DATA_TOKEN);
        isLocalization = getIntent().getBooleanExtra(IS_LOCALIZATION, false);
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
        UserSessionInterface userSession = new UserSession(this);
        if (isLocalization) ChooseAddressTracking.INSTANCE.onClickCloseKotaKecamatan(userSession.getUserId());
        else gtmOnBackPressClicked();
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
}
