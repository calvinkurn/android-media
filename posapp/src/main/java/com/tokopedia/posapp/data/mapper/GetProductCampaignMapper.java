package com.tokopedia.posapp.data.mapper;

import com.tokopedia.core.product.model.productdetail.ProductCampaign;
import com.tokopedia.core.product.model.productdetail.ProductCampaignResponse;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/11/17.
 */

public class GetProductCampaignMapper implements Func1<Response<ProductCampaignResponse>, ProductCampaign>{
    public GetProductCampaignMapper() {

    }

    @Override
    public ProductCampaign call(Response<ProductCampaignResponse> response) {
        return response.body().getData();
    }
}
