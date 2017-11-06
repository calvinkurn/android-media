package com.tokopedia.core.manage.people.address.interactor;

import com.tokopedia.core.manage.people.address.model.districtrecomendation.AddressResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by Irfan Khoirul on 01/11/17.
 */

public interface DistrictRecommendationRetrofitInteractor {

    Observable<AddressResponse> getDistrictRecommendation(TKPDMapParam<String, String> params);

    interface Params {
        String TOKEN = "token";
        String UT = "ut";
        String QUERY = "query";
        String PAGE = "page";
    }

}
