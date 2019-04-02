package com.tokopedia.logisticdata.data.apiservice;

import com.tokopedia.network.constant.TkpdBaseURL;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Irfan Khoirul on 12/12/17.
 */

public interface InsuranceApi {

    @GET(TkpdBaseURL.Shop.PATH_INSURANCE_TERMS_AND_CONDITIONS)
    Observable<String> getInsuranceTnC();

}
