package com.tokopedia.core.network.apiservices.ace.apis;


import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.discovery.BrowseCatalogModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.network.entity.discovery.BrowseShopModel;
import com.tokopedia.core.network.entity.discovery.GuidedSearchResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by noiz354 on 3/17/16.
 * description could be found at https://wiki.tokopedia.net/Search_api
 */

@Deprecated
public interface BrowseApi {

    String DEFAULT_VALUE_OF_PARAMETER_DEVICE = "android";
    String DEFAULT_VALUE_OF_PARAMETER_ROWS = "8";
    String DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE = "200";
    String DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE = "true";
    String DEFAULT_VALUE_OF_PARAMETER_SORT = "23";
    String DEFAULT_VALUE_SOURCE_HOTLIST = "hot_product";
    String DEFAULT_VALUE_SOURCE_SEARCH = "search";
    String DEFAULT_VALUE_SOURCE_DIRECTORY = "directory";
    String DEFAULT_VALUE_SOURCE_PRODUCT = "search_product";
    String DEFAULT_VALUE_SOURCE_CATALOG = "search_catalog";
    String DEFAULT_VALUE_SOURCE_SHOP = "search_shop";
    String DEFAULT_VALUE_SOURCE_QUICK_FILTER = "quick_filter";
    String DEFAULT_VALUE_OF_PARAMETER_TERM = "true";
    String DEFAULT_VALUE_OF_PARAMETER_BREADCRUMB = "false";
    String DEFAULT_VALUE_OF_PARAMETER_START = "0";
    String DEFAULT_VALUE_OF_PARAMETER_SC = "0";

    int CATALOG_OB_RELEASE_DATE = 1;
    int CATALOG_OB_RELEASE_DATE_COUNT_PRODUCT = 2;
    int CATALOG_OB_MOST_PRODUCT = 3;
    int CATALOG_OB_CHEAPEST = 4;
    int CATALOG_OB_MOST_EXPENSIVE = 5;

    int PRODUCT_PROMO = 1;
    int PRODUCT_CHEAPEST = 3;
    int PRODUCT_MOST_EXPENSIVE = 4;
    int PRODUCT_REVIEW = 5;
    int PRODUCT_TALK = 7;
    int PRODUCT_SALES = 8;
    int PRODUCT_NEWEST = 9;
    int PRODUCT_BEST_MATCH = 23;

    String MIDDLE_V1_ACE = "/search/v1/";
    String SEARCH_V2_PRODUCT = "v2.4/product";
    String SEARCH_V1_SHOP = "v1/shop";
    String SEARCH_V1_CATALOG = "v1/catalog";
    String SEARCH_V2_CATALOG = "v2.1/catalog";
    String GUIDE_V1 = "guide/v1";
    String DEVICE = "device";
    String START = "start";
    String ROWS = "rows";
    String IMAGE = "image";
    String SC = "sc"; //  (category id)
    String DEFAULT_SC = "default_sc"; //  (category id)
    String FLOC = "floc"; // (location id)
    String SHIPPING = "shipping"; // (shipping id)
    String SOURCE = "source"; // Source
    String OB = "ob"; // order by value, could be found at wiki
    String PMIN = "pmin";
    String PMAX = "pmax";
    String FSHOP = "fshop";
    String WHOLESALE = "wholesale";
    String CONDITION = "condition";
    String Q = "q"; // (keyword) or (query)
    String H = "hc"; // hotlist id
    String ID = "id";
    String NEGATIVE = "negative";
    String HIGHLIGHT = "highlight";
    String TERMS = "terms";
    String FQ = "fq";
    String ID1 = "-id";
    String SHOP_ID = "shop_id";
    String BREADCRUMB = "breadcrumb";
    String PREORDER = "preorder";
    String IMAGE_SIZE = "image_size";
    String IMAGE_SQUARE = "image_square";
    String HASHTAG = "hashtag";
    String USER_ID = "user_id";
    String UNIQUE_ID = "unique_id";
    String RETURNABLES = "returnables";
    String HOT_ID = "hot_id";
    String REFINED = "rf";
    String RELATED = "related";
    String PAGE = "page";
    String PAGE_SIZE = "page_size";

    /**
     * @param sc
     * @param id
     * @param q
     * @param ob
     * @param pmin
     * @param pmax
     * @param rows
     * @param start
     * @param device
     * @param terms      String empty "", digunakan di catalog contoh di desktop
     *                   https://www.tokopedia.com/p/handphone-tablet/handphone?vi=1
     * @param breadcrumb "true" jika langsung menampilkan breadcumb atau false
     * @return
     */
    @GET(SEARCH_V1_CATALOG)
    Observable<Response<BrowseCatalogModel>> browseCatalogs(
            @Query(SC) String sc,
            @Query(ID) String id,
            @Query(Q) String q,
            @Query(OB) String ob,
            @Query(PMIN) String pmin,
            @Query(PMAX) String pmax,
            @Query(ROWS) String rows,
            @Query(START) String start,
            @Query(DEVICE) String device,
            @Query(TERMS) String terms,// klo terms itu, filter kaya brand, screen dll. kepake d directory product
            @Query(BREADCRUMB) String breadcrumb
    );

    @GET(SEARCH_V1_SHOP)
    Observable<Response<BrowseShopModel>> browseShops(
            @Query(FLOC) String floc,// location
            @Query(Q) String q,// keyword or query to search
            @Query(FSHOP) String fshop,// gold merchant or not
            @Query(ROWS) String rows,// number of rows
            @Query(START) String start,// start index
            @Query(DEVICE) String device// "android"
    );


    /**
     * @param device
     * @param start
     * @param rows
     * @param sc
     * @param floc
     * @param ob         reads wiki for detail
     * @param pmin
     * @param pmax
     * @param fshop      2 for gold merchant, others for non gold merchant
     * @param wholesale, String "true" or "" empty
     * @param q
     * @param id         DIDn't use anymore
     * @param negative   negative keyword di dapat dari, String format
     * @param highlight  DIDN't use anymore
     * @param terms      String empty "", digunakan di catalog contoh di desktop
     *                   https://www.tokopedia.com/p/handphone-tablet/handphone?vi=1
     * @param fq         user dapat search lagi ketika masuk hot / directory user, searchview here
     * @param MinId      digunakan untuk Product Detail, Other Product
     * @param shopId     ini untuk product feed bentuk yang dikirimkan 123,456,128,981,123
     * @return contoh penggunaan other product, product detail :
     * <p>
     * https://ace.tokopedia.com/search/v1/product?shop_id=87419&-id=26703379&rows=5
     * &full_domain=www.tokopedia.com&scheme=https&device=desktop&source=other_product&po=1&start=0
     */
    @GET(SEARCH_V2_PRODUCT)
    Observable<Response<BrowseProductModel>> browseProducts(
            @Query(DEVICE) String device,
            @Query(START) String start,
            @Query(ROWS) String rows,
            @Query(SC) String sc,
            @Query(FLOC) String floc,
            @Query(OB) String ob,
            @Query(PMIN) String pmin,
            @Query(PMAX) String pmax,
            @Query(FSHOP) String fshop,
            @Query(WHOLESALE) String wholesale,
            @Query(Q) String q,
            @Query(ID) String id, // ini single product id
            @Query(NEGATIVE) String negative,//
            @Query(HIGHLIGHT) String highlight,
            @Query(TERMS) String terms,// klo terms itu, filter kaya brand, screen dll. kepake d directory product
            @Query(FQ) String fq,//fq itu search detail di directory / hot product
            @Query(ID1) String MinId,//kalau -id kepake di other product. -id current product
            @Query(SHOP_ID) String shopId,// ini untuk product feed
            @Query(BREADCRUMB) String breadcrumb,
            @Query(PREORDER) String preorder

    );

    @GET(SEARCH_V2_CATALOG)
    Observable<Response<BrowseCatalogModel>> browseCatalogs(
            @QueryMap Map<String, String> query
    );

    @GET(SEARCH_V1_SHOP)
    Observable<Response<BrowseShopModel>> browseShops(
            @QueryMap Map<String, String> query
    );

    @GET(SEARCH_V2_PRODUCT)
    Observable<Response<BrowseProductModel>> browseProducts(
            @QueryMap Map<String, String> query
    );

    @POST(TkpdBaseURL.Ace.PATH_IMAGE_SEARCH)
    Observable<Response<String>> browseImageSearch(@QueryMap TKPDMapParam<String, Object> queryMap,
                                                   @Body TKPDMapParam<String, Object> requestParams
    );

    @GET(TkpdBaseURL.Ace.PATH_SEARCH_PRODUCT)
    Observable<Response<String>> browseProductsV3(
            @QueryMap TKPDMapParam<String, Object> requestParams
    );

    @GET(TkpdBaseURL.Ace.PATH_BROWSE_SHOP)
    Observable<Response<BrowseShopModel>> browseShops(
            @QueryMap TKPDMapParam<String, Object> query
    );

    @GET(TkpdBaseURL.Ace.PATH_GET_ATTRIBUTE)
    Observable<Response<String>> getAttribute(
            @QueryMap TKPDMapParam<String, Object> requestParams
    );

    @GET(TkpdBaseURL.Ace.PATH_GET_DYNAMIC_ATTRIBUTE)
    Observable<Response<String>> getDynamicAttribute(
            @QueryMap TKPDMapParam<String, Object> requestParams
    );

    @GET(TkpdBaseURL.Ace.PATH_GET_DYNAMIC_ATTRIBUTE_V4)
    Observable<Response<String>> getDynamicAttributeV4(
            @QueryMap TKPDMapParam<String, Object> requestParams
    );

    @GET(TkpdBaseURL.Ace.PATH_BROWSE_CATALOG)
    Observable<Response<String>> browseCatalogRevamp(
            @QueryMap TKPDMapParam<String, Object> parameters
    );

    @GET(GUIDE_V1)
    Observable<Response<GuidedSearchResponse>> getGuidedSearch(
            @Query(Q) String query
    );
}
