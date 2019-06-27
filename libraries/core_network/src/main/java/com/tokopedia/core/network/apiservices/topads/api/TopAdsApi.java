package com.tokopedia.core.network.apiservices.topads.api;

import com.tokopedia.core.network.entity.topads.TopAdsResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author noiz354 on 3/23/16.
 *         modified by angga, migrate retrofit 2
 */

@Deprecated
public interface TopAdsApi {

    String PRODUCTS = "products";
    String ITEM = "item";
    String SRC = "src";
    String PAGE = "page";
    String DEP_ID = "dep_id";
    String H = "h";
    String Q = "q";
    String PMIN = "pmin";
    String PMAX = "pmax";
    String FSHOP = "fshop";
    String FLOC = "floc";
    String WHOLESALE = "wholesale";
    String SHIPING = "shipping";
    String PREORDER = "preorder";
    String CONDITION = "condition";
    String RETURNABLE = "freereturns";
    String TKPD_USER_ID = "Tkpd-UserId";
    String TKPD_SESSION_ID = "Tkpd-SessionId";
    String X_DEVICE = "X-Device";

    String SRC_HOTLIST = "hotlist";
    String SRC_BROWSE_PRODUCT = "search";
    String SRC_DIRECTORY = "directory";
    String SRC_PRODUCT_FEED = "fav_product";
    String SRC_FAVORITE_HOME = "fav_shop";
    String SRC_RECOMENDATION = "recommendation";
    String SRC_RECENTLY_VIEWED = "recently_viewed";
    String WISHLIST = "wishlist";

    @GET(PRODUCTS)
    Observable<Response<TopAdsResponse>> getTopAds(
            @Header(X_DEVICE) String xDevice,
            @Header(TKPD_USER_ID) String userId,
            @Header(TKPD_SESSION_ID) String sessionId,
            @QueryMap Map<String, String> params
    );
}
