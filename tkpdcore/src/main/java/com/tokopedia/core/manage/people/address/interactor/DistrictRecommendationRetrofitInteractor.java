package com.tokopedia.core.manage.people.address.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * Created by Irfan Khoirul on 01/11/17.
 */

public interface DistrictRecommendationRetrofitInteractor {

    void getDistrictRecommendation(TKPDMapParam<String, String> params);

    void unSubscribe();

    interface Params {
        String TOKEN = "token";
        String UT = "ut";
        String QUERY = "query";
        String PAGE = "page";
    }

}
