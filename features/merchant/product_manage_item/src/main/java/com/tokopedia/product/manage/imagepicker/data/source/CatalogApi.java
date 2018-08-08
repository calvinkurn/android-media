package com.tokopedia.product.edit.imagepicker.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.product.edit.imagepicker.util.CatalogConstant;
import com.tokopedia.product.edit.imagepicker.data.model.DataResponseCatalogImage;

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
