package com.tokopedia.product.manage.list.data.source;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.product.manage.item.common.data.source.cloud.DataResponse;
import com.tokopedia.product.manage.list.data.model.ResponseDeleteProductData;
import com.tokopedia.product.manage.list.data.model.ResponseEditPriceData;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ActionProductManageDataSourceCloud {

    private final ProductActionApi productActionApi;

    @Inject
    public ActionProductManageDataSourceCloud(ProductActionApi productActionApi) {
        this.productActionApi = productActionApi;
    }

    public Observable<Response<DataResponse<ResponseEditPriceData>>> editPrice(TKPDMapParam<String, String> params) {
        return productActionApi.editPrice(params);
    }

    public Observable<Response<DataResponse<ResponseDeleteProductData>>> deleteProduct(TKPDMapParam<String, String> paramsNetwork) {
        return productActionApi.delete(paramsNetwork);
    }
}
