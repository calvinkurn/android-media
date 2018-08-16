package com.tokopedia.district_recommendation.view.shopsettings;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.shop.MyShopAddressService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.district_recommendation.R;
import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.district_recommendation.view.DistrictRecommendationActivity;
import com.tokopedia.district_recommendation.view.DistrictRecommendationFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/** temporary implementation to handle request token first in district recommendation page
 *  for the case this acticity is called without token beforehand
 */
public class DistrictRecommendationShopSettingsActivity extends DistrictRecommendationActivity {
    private TkpdProgressDialog progressDialog;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public static Intent createInstance(Activity activity) {
        return new Intent(activity, DistrictRecommendationShopSettingsActivity.class);
    }

    @Override
    protected void initView() {
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS, getWindow().getDecorView().getRootView());
        progressDialog.setLoadingViewId(R.id.include_loading);
        progressDialog.showDialog();
        requestGetToken();
    }

    private void requestGetToken() {
        compositeSubscription.add(new MyShopAddressService().getApi().getLocation(
                AuthUtil.generateParams(this, new HashMap<String, String>())
        ).subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                NetworkErrorHelper.showEmptyState( DistrictRecommendationShopSettingsActivity.this,
                                        findViewById(R.id.container), new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        requestGetToken();
                                    }
                                });
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                TkpdResponse response = responseData.body();
                                try {
                                    JSONObject jsonObject = new JSONObject(response.getStringData());

                                    Gson gson = new GsonBuilder().create();
                                    ShopAddressTokenResponse data =
                                            gson.fromJson(jsonObject.toString(), ShopAddressTokenResponse.class);
                                    showView(data.getToken());

                                } catch (JSONException je) { }
                            }
                        }
                ));
    }

    private void showView(Token token) {
        DistrictRecommendationShopSettingsFragment fragment = DistrictRecommendationShopSettingsFragment.newInstance(token);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment,
                DistrictRecommendationFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    class ShopAddressTokenResponse{
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
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }
}
