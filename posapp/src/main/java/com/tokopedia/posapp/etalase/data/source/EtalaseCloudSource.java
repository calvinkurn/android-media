package com.tokopedia.posapp.etalase.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.etalase.data.mapper.GetEtalaseMapper;
import com.tokopedia.posapp.product.common.data.source.cloud.ProductApi;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 10/17/17.
 */

public class EtalaseCloudSource {
    private ProductApi productApi;
    private GetEtalaseMapper getEtalaseMapper;

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
