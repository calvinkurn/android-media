package com.tokopedia.core.network.apiservices.ace.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.hotlist.HotListResponse;
import com.tokopedia.core.product.model.productotherace.ProductOtherDataAce;
import com.tokopedia.core.shopinfo.models.productmodel.ShopProductResult;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Angga.Prasetiyo on 19/01/2016.
 */
public interface SearchApi {


    @GET(TkpdBaseURL.Ace.PATH_OTHER_PRODUCT)
    Observable<Response<ProductOtherDataAce>> getOtherProducts(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Ace.PATH_OTHER_PRODUCT)
    Observable<Response<ShopProductResult>> getShopProduct(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Ace.PATH_TOP_PICKS)
    Observable<Response<String>> getTopPicks(@QueryMap Map<String, String> params,
                                             @Header("X-APP-VERSION") String version,
                                             @Header("X-DEVICE") String device);

    @GET(TkpdBaseURL.Ace.PATH_HOTLIST_CATEGORY)
    Observable<Response<HotListResponse>> getHotlistCategory(@QueryMap Map<String, String> params);
}
