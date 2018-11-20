package com.tokopedia.district_recommendation.view.v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.district_recommendation.R;
import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.district_recommendation.domain.usecase.GetShopAddressUseCase;
import com.tokopedia.district_recommendation.view.ITransactionAnalyticsDistrictRecommendation;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.user.session.UserSession;

import static com.tokopedia.district_recommendation.view.DistrictRecommendationContract.Constant.ARGUMENT_DATA_TOKEN;

/**
 * Created by Irfan Khoirul on 17/11/18.
 */

public class DistrictRecommendationActivity extends BaseSimpleActivity
        implements HasComponent, ITransactionAnalyticsDistrictRecommendation, FragmentListener {

    private CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;
    protected UserSession userSession;
    protected GetShopAddressUseCase getShopAddressUseCase;

    public static Intent createInstanceIntent(Activity activity, Token token) {
        Intent intent = new Intent(activity, DistrictRecommendationActivity.class);
        intent.putExtra(ARGUMENT_DATA_TOKEN, token);
        return intent;
    }

    public static Intent createInstanceFromMarketplaceCart(Activity activity, Token token) {
        Intent intent = new Intent(activity, DistrictRecommendationActivity.class);
        intent.putExtra(ARGUMENT_DATA_TOKEN, token);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplication() instanceof AbstractionRouter) {
            checkoutAnalyticsChangeAddress = new CheckoutAnalyticsChangeAddress(
                    ((AbstractionRouter) getApplication()).getAnalyticTracker()
            );
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            toolbar.setNavigationIcon(R.drawable.ic_clear_24dp);
        }

    }

    @Override
    protected Fragment getNewFragment() {
        Token token = getIntent().getParcelableExtra(ARGUMENT_DATA_TOKEN);
        return DistrictRecommendationFragment.newInstance(token);
    }

    @Override
    public BaseAppComponent getComponent() {
        return ((MainApplication) getApplication()).getBaseAppComponent();
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

    @Override
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public void setShopAddressUseCase(GetShopAddressUseCase shopAddressUseCase) {
        this.getShopAddressUseCase = shopAddressUseCase;
    }

}
