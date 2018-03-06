package com.tokopedia.posapp.cache.data.factory;

import com.tokopedia.posapp.etalase.GetEtalaseMapper;
import com.tokopedia.posapp.product.productlist.data.source.cloud.ProductListApi;
import com.tokopedia.posapp.etalase.EtalaseCloudSource;
import com.tokopedia.posapp.cache.data.source.local.EtalaseLocalSource;

/**
 * Created by okasurya on 9/25/17.
 */
@Deprecated
public class EtalaseFactory {
    private GetEtalaseMapper getEtalaseMapper;
    private ProductListApi productListApi;

    public EtalaseFactory(ProductListApi productListApi, GetEtalaseMapper getEtalaseMapper) {
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
