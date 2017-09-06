package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.product.model.productdetail.ProductCampaign;
import com.tokopedia.core.product.model.productdetail.ProductCampaignResponse;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.data.mapper.GetProductMapper;
import com.tokopedia.posapp.data.source.cloud.api.ProductApi;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductCloudSource {
    public static final String PRODUCT_ID = "PRODUCT_ID";

    private ProductApi productApi;
    private MojitoApi mojitoApi;
    private GetProductMapper getProductMapper;

    public ProductCloudSource(ProductApi productApi,
                              MojitoApi mojitoApi,
                              GetProductMapper getProductMapper) {
        this.productApi = productApi;
        this.mojitoApi = mojitoApi;
        this.getProductMapper = getProductMapper;
    }

    public Observable<ProductDetailData> getProduct(RequestParams params) {
        return productApi.getProductDetail(params.getParamsAllValueInString()).map(getProductMapper);
    }

    public Observable<ProductCampaign> getProductCampaign(RequestParams params) {
        return mojitoApi
                .getProductCampaign(params.getString(PRODUCT_ID, ""))
                .map(mapToProductCampaign());
    }

    private Func1<Response<ProductCampaignResponse>, ProductCampaign> mapToProductCampaign() {
        return new Func1<Response<ProductCampaignResponse>, ProductCampaign>() {
            @Override
            public ProductCampaign call(Response<ProductCampaignResponse> response) {
                return response.body().getData();
            }
        };
    }
}
