package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.data.mapper.GetProductCampaignMapper;
import com.tokopedia.posapp.data.mapper.GetProductMapper;
import com.tokopedia.posapp.data.source.cloud.api.ProductApi;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductCloudSource {
    public static final String PRODUCT_ID = "PRODUCT_ID";

    private ProductApi productApi;
    private MojitoApi mojitoApi;
    private GetProductMapper getProductMapper;
    private GetProductCampaignMapper getProductCampaignMapper;

    public ProductCloudSource(ProductApi productApi,
                              MojitoApi mojitoApi,
                              GetProductMapper getProductMapper,
                              GetProductCampaignMapper getProductCampaignMapper) {
        this.productApi = productApi;
        this.mojitoApi = mojitoApi;
        this.getProductMapper = getProductMapper;
        this.getProductCampaignMapper = getProductCampaignMapper;
    }

    public Observable<ProductDetailData> getProduct(RequestParams params) {
        return productApi.getProductDetail(params.getParamsAllValueInString()).map(getProductMapper);
    }

    public Observable getProductCampaign(RequestParams params) {
        return mojitoApi
                .getProductCampaign(params.getString(PRODUCT_ID, ""))
                .map(getProductCampaignMapper);
    }
}
