package com.tokopedia.logisticaddaddress.features.district_recommendation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.logisticaddaddress.di.DaggerDistrictRecommendationComponent;
import com.tokopedia.logisticaddaddress.di.DistrictRecommendationComponent;
import com.tokopedia.logisticaddaddress.domain.mapper.TokenMapper;
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecomToken;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.logisticaddaddress.features.district_recommendation.DistrictRecommendationContract.Constant.ARGUMENT_DATA_TOKEN;

/**
 * Created by Irfan Khoirul on 17/11/18.
 * Deeplink: DISTRICT_RECOMMENDATION_SHOP_SETTINGS
 */

public class DistrictRecommendationActivity extends BaseSimpleActivity
        implements HasComponent, ITransactionAnalyticsDistrictRecommendation {

    private CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;

    @Inject
    GetDistrictRecomToken getTokenUsecase;

    @Inject
    UserSessionInterface userSession;

    public static Intent createInstanceIntent(Activity activity, com.tokopedia.logisticaddaddress.domain.model.Token token) {
        return newInstance(activity, new TokenMapper().convertTokenModel(token));
    }

    public static Intent newInstance(Activity activity, Token token) {
        Intent intent = new Intent(activity, DistrictRecommendationActivity.class);
        intent.putExtra(ARGUMENT_DATA_TOKEN, token);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkoutAnalyticsChangeAddress = new CheckoutAnalyticsChangeAddress();
        DistrictRecommendationComponent districtRecommendationComponent =
                DaggerDistrictRecommendationComponent.builder()
                        .baseAppComponent(getComponent())
                        .build();
        districtRecommendationComponent.inject(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            toolbar.setNavigationIcon(R.drawable.ic_close);
        }

        Token token = getIntent().getParcelableExtra(ARGUMENT_DATA_TOKEN);
        if (token != null) {
            initFragment(token);
        } else {
            requestGetToken();
        }

    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
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

    private void requestGetToken() {
        Map<String, String> params = new HashMap<>();
        params = AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), params);

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(GetDistrictRecomToken.PARAM_AUTH, params);

        ProgressDialog progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage(getString(R.string.title_loading));
        progress.setIndeterminate(true);
        progress.show();

        getTokenUsecase.execute(requestParams, new Subscriber<Token>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                progress.dismiss();
                showErrorState();
            }

            @Override
            public void onNext(Token token) {
                progress.dismiss();
                if (token != null) {
                    initFragment(token);
                } else {
                    showErrorState();
                }
            }
        });
    }

    private void initFragment(Token token) {
        Fragment fragment = DistrictRecommendationFragment.newInstance(token);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.parent_view, fragment, getTagFragment())
                .commit();
    }

    private void showErrorState() {
        NetworkErrorHelper.showEmptyState(this, findViewById(R.id.container), this::requestGetToken);
    }

}
