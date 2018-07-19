package com.tokopedia.district_recommendation.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.district_recommendation.R;
import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;

import static com.tokopedia.district_recommendation.view.DistrictRecommendationContract.Constant.ARGUMENT_DATA_TOKEN;
import static com.tokopedia.district_recommendation.view.DistrictRecommendationContract.Constant.ARGUMENT_IS_FROM_MARKETPLACE_CART;

public class DistrictRecommendationActivity extends BasePresenterActivity implements
        DistrictRecommendationContract.IAnalyticsDistrictRecommendation {

    private CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;

    public static Intent createInstance(Activity activity, Token token) {
        Intent intent = new Intent(activity, DistrictRecommendationActivity.class);
        intent.putExtra(ARGUMENT_DATA_TOKEN, token);
        intent.putExtra(ARGUMENT_IS_FROM_MARKETPLACE_CART, false);
        return intent;
    }

    public static Intent createInstanceFromMarketplaceCart(Activity activity, Token token) {
        Intent intent = new Intent(activity, DistrictRecommendationActivity.class);
        intent.putExtra(ARGUMENT_DATA_TOKEN, token);
        intent.putExtra(ARGUMENT_IS_FROM_MARKETPLACE_CART, true);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        Token token = getIntent().getParcelableExtra(ARGUMENT_DATA_TOKEN);
        DistrictRecommendationFragment fragment = DistrictRecommendationFragment.newInstance(token);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment,
                DistrictRecommendationFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        if (getApplication() instanceof AbstractionRouter) {
            checkoutAnalyticsChangeAddress = new CheckoutAnalyticsChangeAddress(
                    ((AbstractionRouter) getApplication()).getAnalyticTracker()
            );
        }
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_clear_24dp);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void onBackPressed() {
        sendAnalyticsOnBackPressClicked();
        super.onBackPressed();
    }

    @Override
    public void sendAnalyticsOnBackPressClicked() {
        checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickXPojokKiriKotaAtauKecamatanPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnDistrictDropdownSelectionItemClicked(String districtName) {
        checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickChecklistKotaAtauKecamatanPadaTambahAddress(districtName);
    }

    @Override
    public void sendAnalyticsOnClearTextDistrictRecommendationInput() {
        checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickXPojokKananKotaAtauKecamatanPadaTambahAddress();
    }
}
