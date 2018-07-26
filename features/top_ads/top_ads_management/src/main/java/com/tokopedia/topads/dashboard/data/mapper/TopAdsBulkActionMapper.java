package com.tokopedia.topads.dashboard.data.mapper;

import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.seller.common.data.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/1/17.
 */
public class TopAdsBulkActionMapper implements Func1<Response<DataResponse<ProductAdBulkAction>>, ProductAdBulkAction> {

    @Inject
    public TopAdsBulkActionMapper() {
    }

    @Override
    public ProductAdBulkAction call(Response<DataResponse<ProductAdBulkAction>> dataResponseResponse) {
        return mappingresponse(dataResponseResponse);
    }

    private ProductAdBulkAction mappingresponse(Response<DataResponse<ProductAdBulkAction>> dataResponseResponse) {
        if (dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null
                && dataResponseResponse.body().getData() != null) {
            return dataResponseResponse.body().getData();
        } else {
            return null;
        }
    }
}
