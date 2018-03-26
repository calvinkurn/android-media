package com.tokopedia.posapp.cache.data.factory;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.posapp.etalase.GetEtalaseMapper;
import com.tokopedia.posapp.product.productlist.data.source.cloud.ProductListApi;
import com.tokopedia.posapp.etalase.EtalaseCloudSource;
import com.tokopedia.posapp.cache.data.source.local.EtalaseLocalSource;

import javax.inject.Inject;
import javax.inject.Named;

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
