package com.tokopedia.posapp.data.factory;

import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.posapp.data.mapper.GetProductCampaignMapper;
import com.tokopedia.posapp.data.mapper.GetProductMapper;
import com.tokopedia.posapp.data.source.cloud.ProductCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.ProductApi;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductFactory {
    private ProductApi productApi;
    private MojitoApi mojitoApi;
    private GetProductMapper getProductMapper;
    private GetProductCampaignMapper getProductCampaignMapper;

    public ProductFactory(ProductApi productApi,
                          MojitoApi mojitoApi,
                          GetProductMapper getProductMapper,
                          GetProductCampaignMapper getProductCampaignMapper) {
        this.productApi = productApi;
        this.mojitoApi = mojitoApi;
        this.getProductMapper = getProductMapper;
        this.getProductCampaignMapper = getProductCampaignMapper;
    }

    public ProductCloudSource productFromCloud() {
        return new ProductCloudSource(productApi, mojitoApi, getProductMapper, getProductCampaignMapper);
    }
}
