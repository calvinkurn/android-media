package com.tokopedia.core.network.apiservices.mojito.apis;


import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.intermediary.brands.MojitoBrandsModel;
import com.tokopedia.core.network.entity.wishlist.WishlistCheckResult;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ricoharisin on 4/15/16.
 * Modified by Mady add HomeCategoryMenu
 */

@Deprecated
public interface MojitoApi {

    String DEVICE = "device";
    String ID = "id";
    String CATEGORY_ID = "categoryId";

    @GET(TkpdBaseURL.Mojito.API_V1_BRANDS_CATEGORY)
    Observable<Response<MojitoBrandsModel>> getBrandsCategory(
            @Path(CATEGORY_ID) String categoryID
    );

    @GET(TkpdBaseURL.Mojito.PATH_OS_BANNER)
    Observable<Response<String>> getOfficialStoreBanner(@Query("keywords") String keyword);

    @GET(TkpdBaseURL.Mojito.PATH_CHECK_WISHLIST)
    Observable<Response<WishlistCheckResult>> checkWishlist(
            @Path("userId") String userId,
            @Path("listId") String listId);
}