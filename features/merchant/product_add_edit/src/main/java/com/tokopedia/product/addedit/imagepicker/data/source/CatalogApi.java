package com.tokopedia.product.addedit.imagepicker.data.source;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.product.addedit.imagepicker.data.model.DataResponseCatalogImage;
import com.tokopedia.product.addedit.imagepicker.util.CatalogConstant;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public interface CatalogApi {
    @GET(CatalogConstant.URL_GET_CATALOG_IMAGE )
    Observable<Response<DataResponse<DataResponseCatalogImage>>> getCatalogImage(@Query(CatalogConstant.CATALOG_ID_EXTRAS) String catalogId);
}
