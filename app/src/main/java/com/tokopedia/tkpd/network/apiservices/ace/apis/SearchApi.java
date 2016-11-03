package com.tokopedia.tkpd.network.apiservices.ace.apis;

import com.tokopedia.tkpd.myproduct.model.CatalogDataModel;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.product.model.productotherace.ProductOtherDataAce;
import com.tokopedia.tkpd.shopinfo.models.productmodel.ShopProductResult;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Angga.Prasetiyo on 19/01/2016.
 */
public interface SearchApi {

    @GET(TkpdBaseURL.Ace.PATH_SEARCH_SHOP)
    Observable<String> searchShop(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Ace.PATH_CATALOG_SHOP_LIST)
    Observable<String> getCatalogShopList(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Ace.PATH_OTHER_PRODUCT)
    Observable<Response<ProductOtherDataAce>> getOtherProducts(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Ace.PATH_FAV_SHOP_FEED)
    Observable<String> getFavShopFeed(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Ace.PATH_OTHER_PRODUCT)
    Observable<Response<ShopProductResult>> getShopProduct(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Ace.PATH_CATALOG)
    Observable<CatalogDataModel> getCatalog(@QueryMap Map<String, String> params);
}
