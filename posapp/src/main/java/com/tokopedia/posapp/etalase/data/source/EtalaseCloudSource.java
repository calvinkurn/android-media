package com.tokopedia.posapp.etalase.data.source;

import com.tokopedia.posapp.etalase.data.mapper.GetEtalaseMapper;
import com.tokopedia.posapp.product.common.data.source.cloud.ProductApi;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 10/17/17.
 */

public class EtalaseCloudSource {
    private ProductApi productApi;
    private GetEtalaseMapper getEtalaseMapper;

    @Inject
    public EtalaseCloudSource(ProductApi productApi,
                              GetEtalaseMapper getEtalaseMapper) {
        this.productApi = productApi;
        this.getEtalaseMapper = getEtalaseMapper;
    }

    public Observable<List<EtalaseDomain>> getEtalase(RequestParams params) {
        return productApi
                .getEtalase(params.getParamsAllValueInString())
                .map(getEtalaseMapper);
    }
}
