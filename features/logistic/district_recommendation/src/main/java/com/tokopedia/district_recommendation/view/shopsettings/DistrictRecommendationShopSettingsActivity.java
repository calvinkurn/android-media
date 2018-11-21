package com.tokopedia.district_recommendation.view.shopsettings;

import android.app.Activity;
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
import com.tokopedia.logisticcommon.utils.TkpdProgressDialog;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;

/**
 * temporary implementation to handle request token first in district recommendation page
 * for the case this acticity is called without token beforehand
 */
public class DistrictRecommendationShopSettingsActivity extends DistrictRecommendationActivity {
    private TkpdProgressDialog progressDialog;

    @Inject
    GetShopAddressUseCase getShopAddressUseCase;

    @Inject
    UserSession userSession;

    public static Intent createInstance(Activity activity) {
        return new Intent(activity, DistrictRecommendationShopSettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();
        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS, getWindow().getDecorView().getRootView());
        // Todo : resolve this
//        progressDialog.setLoadingViewId(R.id.include_loading);
        progressDialog.showDialog();
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
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params = AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), params);

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(GetShopAddressUseCase.PARAM_AUTH, params);

        getShopAddressUseCase.execute(requestParams, new Subscriber<Response<TokopediaWsV4Response>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                NetworkErrorHelper.showEmptyState(DistrictRecommendationShopSettingsActivity.this,
                        findViewById(R.id.container), new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                requestGetToken();
                            }
                        });
            }

            @Override
            public void onNext(Response<TokopediaWsV4Response> tokopediaWsV4ResponseResponse) {
                TokopediaWsV4Response response = tokopediaWsV4ResponseResponse.body();
                try {
                    JSONObject jsonObject = new JSONObject(response.getStringData());

                    Gson gson = new GsonBuilder().create();
                    ShopAddressTokenResponse data =
                            gson.fromJson(jsonObject.toString(), ShopAddressTokenResponse.class);
                    showView(data.getToken());

                } catch (JSONException je) {
                }
            }
        });
    }

    private void showView(Token token) {
        DistrictRecommendationShopSettingsFragment fragment = DistrictRecommendationShopSettingsFragment.newInstance(token);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.parent_view, fragment,
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
