package com.tokopedia.topads.common.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.topads.common.data.api.TopAdsManagementApi;
import com.tokopedia.topads.common.data.model.DataCheckPromo;
import com.tokopedia.usecase.RequestParams;

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

    private final TopAdsManagementApi topAdsManagementApi;

    public TopAdsCheckProductPromoDataSourceCloud(TopAdsManagementApi topAdsManagementApi) {
        this.topAdsManagementApi = topAdsManagementApi;
    }

    public Observable<DataCheckPromo> checkPromoAds(RequestParams requestParams) {
        requestParams.putString(PARAM_ADS_TYPE, ADS_TYPE_PRODUCT);
        requestParams.putString(PARAM_DEVICE_ID, "android");
        return topAdsManagementApi.checkPromoAds(requestParams.getParamsAllValueInString())
                .map(new Func1<Response<DataResponse<DataCheckPromo>>, DataCheckPromo>() {
                    @Override
                    public DataCheckPromo call(Response<DataResponse<DataCheckPromo>> dataResponse) {
                        if (dataResponse.isSuccessful()){
                            return dataResponse.body().getData();
                        } else {
                            return null;
                        }
                    }
                });
    }
}
