package com.tokopedia.discovery.imagesearch.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.network.entity.discovery.ImageSearchProductResponse;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.discovery.newdiscovery.data.mapper.ProductMapper;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;

import rx.functions.Func1;

/**
 * Created by sachinbansal on 5/29/18.
 */

public class ImageProductMapper implements Func1<GraphqlResponse<ImageSearchProductResponse>, SearchResultModel> {

    private final Gson gson;

    private ProductMapper productMapper;

    public ImageProductMapper(Gson gson, ProductMapper productMapper) {
        this.gson = gson;
        this.productMapper = productMapper;
    }

    @Override
    public SearchResultModel call(GraphqlResponse<ImageSearchProductResponse> response) {
        if (response != null) {
            try {
                ImageSearchProductResponse searchProductResponse = response.getData();
                return productMapper.mappingPojoIntoDomain(searchProductResponse.getSearchProductResponse());
            } catch (Exception e) {
                throw new RuntimeHttpErrorException(430);
            }
        } else {
            throw new RuntimeHttpErrorException(432);
        }
    }

}

