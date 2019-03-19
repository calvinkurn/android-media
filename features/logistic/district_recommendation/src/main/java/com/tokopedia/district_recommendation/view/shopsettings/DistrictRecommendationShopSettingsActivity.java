package com.tokopedia.district_recommendation.view.shopsettings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.district_recommendation.R;
import com.tokopedia.district_recommendation.di.DaggerDistrictRecommendationComponent;
import com.tokopedia.district_recommendation.di.DistrictRecommendationComponent;
import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.district_recommendation.domain.usecase.GetShopAddressUseCase;
import com.tokopedia.district_recommendation.view.DistrictRecommendationActivity;
import com.tokopedia.district_recommendation.view.DistrictRecommendationFragment;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;

/**
 * temporary implementation to handle request token first in district recommendation page
 * for the case this acticity is called without token beforehand
 */
public class DistrictRecommendationShopSettingsActivity extends DistrictRecommendationActivity {

    @Inject
    GetShopAddressUseCase getShopAddressUseCase;

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
        requestParams.putObject(GetShopAddressUseCase.PARAM_AUTH, params);

        ProgressDialog progress = new ProgressDialog(this, com.tokopedia.logisticcommon.R.style.CoolDialog);
        progress.show();
        progress.setContentView(com.tokopedia.logisticcommon.R.layout.loader_logistic_module);
        progress.setCancelable(false);
        progress.setOnCancelListener(dialog -> (this).finish());

        getShopAddressUseCase.execute(requestParams, new Subscriber<Response<TokopediaWsV4Response>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                showErrorState();
            }

            @Override
            public void onNext(Response<TokopediaWsV4Response> tokopediaWsV4ResponseResponse) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                TokopediaWsV4Response response = tokopediaWsV4ResponseResponse.body();
                try {
                    JSONObject jsonObject = new JSONObject(response.getStringData());

                    Gson gson = new GsonBuilder().create();
                    ShopAddressTokenResponse data =
                            gson.fromJson(jsonObject.toString(), ShopAddressTokenResponse.class);
                    if (data != null && data.getToken() != null) {
                        showView(data.getToken());
                    } else {
                        showErrorState();
                    }

                } catch (JSONException je) {
                    je.printStackTrace();
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

    class ShopAddressTokenResponse {
        @SerializedName("token")
        @Expose
        private Token token;

        public Token getToken() {
            return token;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getShopAddressUseCase.unsubscribe();
    }
}
