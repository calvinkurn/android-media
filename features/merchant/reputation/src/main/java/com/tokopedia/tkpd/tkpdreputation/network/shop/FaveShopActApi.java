package com.tokopedia.tkpd.tkpdreputation.network.shop;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface FaveShopActApi {

    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_FAVE_SHOP)
    Observable<Response<TokopediaWsV4Response>> faveShop(@FieldMap Map<String, String> params);

}
