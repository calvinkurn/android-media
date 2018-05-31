package com.tokopedia.instantloan.data.soruce.cloud.api;

import com.google.gson.JsonObject;
import com.tokopedia.instantloan.data.model.response.ResponseBannerOffer;
import com.tokopedia.instantloan.data.model.response.ResponseLoanProfileStatus;
import com.tokopedia.instantloan.data.model.response.ResponsePhoneData;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

import static com.tokopedia.instantloan.network.InstantLoanUrl.PATH_BANNER_OFFER;
import static com.tokopedia.instantloan.network.InstantLoanUrl.PATH_POST_PHONEDATA;
import static com.tokopedia.instantloan.network.InstantLoanUrl.PATH_USER_STATUS;

/**
 * Created by lavekush on 20/03/18.
 */

public interface InstantLoanApi {
    @GET(PATH_USER_STATUS)
    Observable<Response<ResponseLoanProfileStatus>> getStatus();

    @POST(PATH_POST_PHONEDATA)
    Observable<Response<ResponsePhoneData>> postPhoneData(@Body JsonObject data);

    @GET(PATH_BANNER_OFFER)
    Observable<Response<ResponseBannerOffer>> getBanners();
}
