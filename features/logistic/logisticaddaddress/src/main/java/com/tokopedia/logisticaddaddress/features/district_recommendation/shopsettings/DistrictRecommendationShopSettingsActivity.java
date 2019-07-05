package com.tokopedia.logisticaddaddress.features.district_recommendation.shopsettings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.logisticaddaddress.di.DaggerDistrictRecommendationComponent;
import com.tokopedia.logisticaddaddress.di.DistrictRecommendationComponent;
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecomToken;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DistrictRecommendationActivity;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DistrictRecommendationFragment;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * temporary implementation to handle request token first in district recommendation page
 * for the case this acticity is called without token beforehand
 * Deeplink: DISTRICT_RECOMMENDATION_SHOP_SETTINGS
 */
public class DistrictRecommendationShopSettingsActivity extends DistrictRecommendationActivity {

    @Inject
    GetDistrictRecomToken getTokenUsecase;

    @Inject
    UserSessionInterface userSession;

    public static Intent createInstance(Activity activity) {
        return new Intent(activity, DistrictRecommendationShopSettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();
        requestGetToken();
    }

    private void initializeInjector() {
        BaseAppComponent baseAppComponent = ((BaseMainApplication) getApplication()).getBaseAppComponent();
        DistrictRecommendationComponent districtRecommendationComponent =
                DaggerDistrictRecommendationComponent.builder()
                        .baseAppComponent(baseAppComponent)
                        .build();
        districtRecommendationComponent.inject(this);
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
                    showView(token);
                } else {
                    showErrorState();
                }
            }
        });
    }

    private void showErrorState() {
        NetworkErrorHelper.showEmptyState(DistrictRecommendationShopSettingsActivity.this,
                findViewById(R.id.container), this::requestGetToken);
    }

    private void showView(Token token) {
        DistrictRecommendationShopSettingsFragment fragment = DistrictRecommendationShopSettingsFragment.newInstance(token);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.replace(R.id.parent_view, fragment,
                DistrictRecommendationFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getTokenUsecase.unsubscribe();
    }
}
