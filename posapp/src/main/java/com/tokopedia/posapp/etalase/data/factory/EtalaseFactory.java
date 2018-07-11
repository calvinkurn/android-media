package com.tokopedia.posapp.etalase.data.factory;


import com.tokopedia.posapp.etalase.data.mapper.GetEtalaseMapper;
import com.tokopedia.posapp.etalase.data.source.EtalaseCloudSource;
import com.tokopedia.posapp.etalase.data.source.EtalaseLocalSource;
import com.tokopedia.posapp.product.common.data.source.cloud.ProductApi;

import javax.inject.Inject;

/**
 * Created by okasurya on 9/25/17.
 */
@Deprecated
public class EtalaseFactory {
    private GetEtalaseMapper getEtalaseMapper;
    private ProductApi productApi;

    @Inject
    public EtalaseFactory(ProductApi productApi,
                          GetEtalaseMapper getEtalaseMapper) {
        this.productApi = productApi;
        this.getEtalaseMapper = getEtalaseMapper;
    }

    public EtalaseCloudSource cloud() {
        return new EtalaseCloudSource(productApi, getEtalaseMapper);
    }

    public EtalaseLocalSource local() {
        return new EtalaseLocalSource();
    }
}
