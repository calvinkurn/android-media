package com.tokopedia.topads.dashboard.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.product.apis.PromoTopAdsApi;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hadi-putra on 11/04/18.
 */

public class TopAdsCheckProductPromoDataSourceCloud {
    private static final String PARAM_ADS_TYPE = "type";
    private static final String PARAM_DEVICE_ID = "device";

    private static final String ADS_TYPE_PRODUCT = "1";

    private final PromoTopAdsApi promoTopAdsApi;

    public TopAdsCheckProductPromoDataSourceCloud(PromoTopAdsApi promoTopAdsApi) {
        this.promoTopAdsApi = promoTopAdsApi;
    }

    public Observable<String> checkPromoAds(RequestParams requestParams) {
        requestParams.putString(PARAM_ADS_TYPE, ADS_TYPE_PRODUCT);
        requestParams.putString(PARAM_DEVICE_ID, "android");
        return promoTopAdsApi.checkPromoAds(requestParams.getParamsAllValueInString())
                .map(new Func1<Response<String>, String>() {
                    @Override
                    public String call(Response<String> stringResponse) {
                        if (stringResponse.isSuccessful()){
                            try {
                                JSONObject object = new JSONObject(stringResponse.body());
                                return object.getJSONObject("data").getString("ad_id");
                            } catch (JSONException e) {
                                return null;
                            }
                        }
                        return null;
                    }
                });
    }
}
