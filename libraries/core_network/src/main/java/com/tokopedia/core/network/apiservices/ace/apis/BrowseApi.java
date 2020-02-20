package com.tokopedia.core.network.apiservices.ace.apis;


import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import retrofit2.http.GET;
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
    String DEFAULT_VALUE_SOURCE_CATALOG = "search_catalog";
    String DEFAULT_VALUE_OF_PARAMETER_START = "0";
    String DEFAULT_VALUE_OF_PARAMETER_SC = "0";

    String DEVICE = "device";
    String START = "start";
    String ROWS = "rows";
    String SC = "sc"; //  (category id)
    String DEFAULT_SC = "default_sc"; //  (category id)
    String SOURCE = "source"; // Source
    String OB = "ob"; // order by value, could be found at wiki
    String PMIN = "pmin";
    String PMAX = "pmax";
    String FSHOP = "fshop";
    String Q = "q"; // (keyword) or (query)
    String H = "hc"; // hotlist id
    String ID = "id";
    String NEGATIVE = "negative";
    String TERMS = "terms";
    String SHOP_ID = "shop_id";
    String IMAGE_SIZE = "image_size";
    String IMAGE_SQUARE = "image_square";
    String USER_ID = "user_id";
    String UNIQUE_ID = "unique_id";
    String HOT_ID = "hot_id";
    String PAGE = "page";
    String IS_CURATED = "is_curated";

    @GET(TkpdBaseURL.Ace.PATH_SEARCH_PRODUCT)
    Observable<Response<String>> browseProductsV3(
            @QueryMap TKPDMapParam<String, Object> requestParams
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
}
