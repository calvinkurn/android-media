package com.tokopedia.gm.featured.data.cloud.api;

import com.tokopedia.gm.featured.constant.GMFeaturedConstant;
import com.tokopedia.gm.featured.data.model.GMFeaturedProductDataModel;
import com.tokopedia.gm.featured.data.model.GMFeaturedProductModel;
import com.tokopedia.gm.featured.data.model.GMFeaturedProductSubmitModel;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public interface GMFeaturedProductApi {
    @GET(GMFeaturedConstant.GET_FEATURED_PRODUCT_URL)
    Observable<Response<GMFeaturedProductDataModel>> getFeaturedProduct(@Path("id") String shopId);

    @POST(GMFeaturedConstant.POST_FEATURED_PRODUCT_URL)
    Observable<Response<GMFeaturedProductModel>> postFeaturedProduct(@Body GMFeaturedProductSubmitModel GMFeaturedProductSubmitModel);
}
