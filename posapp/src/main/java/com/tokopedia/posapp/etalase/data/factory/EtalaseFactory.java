package com.tokopedia.posapp.etalase.data.factory;


import com.tokopedia.posapp.etalase.data.mapper.GetEtalaseMapper;
import com.tokopedia.posapp.etalase.data.source.EtalaseCloudSource;
import com.tokopedia.posapp.etalase.data.source.EtalaseLocalSource;
import com.tokopedia.posapp.product.productlist.data.source.cloud.ProductListApi;

import javax.inject.Inject;

/**
 * Created by okasurya on 9/25/17.
 */
@Deprecated
public class EtalaseFactory {
    private GetEtalaseMapper getEtalaseMapper;
    private ProductListApi productListApi;

    @Inject
    public EtalaseFactory(ProductListApi productListApi,
                          GetEtalaseMapper getEtalaseMapper) {
        this.productListApi = productListApi;
        this.getEtalaseMapper = getEtalaseMapper;
    }

    public EtalaseCloudSource cloud() {
        return new EtalaseCloudSource(productListApi, getEtalaseMapper);
    }

    public EtalaseLocalSource local() {
        return new EtalaseLocalSource();
    }
}
