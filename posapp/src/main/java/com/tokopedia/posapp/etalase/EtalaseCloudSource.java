package com.tokopedia.posapp.etalase;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.product.productlist.data.source.cloud.ProductListApi;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;

import java.util.List;

import javax.inject.Named;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by okasurya on 10/17/17.
 */

public class EtalaseCloudSource {
    private static final String SHOP_ID = "shop_id";

    private ProductListApi productListApi;
    private GetEtalaseMapper getEtalaseMapper;

    public EtalaseCloudSource(ProductListApi productListApi,
                              GetEtalaseMapper getEtalaseMapper) {
        this.productListApi = productListApi;
        this.getEtalaseMapper = getEtalaseMapper;
    }

    public Observable<List<EtalaseDomain>> getEtalase(RequestParams params) {
        return productListApi
                .getEtalase(params.getParamsAllValueInString())
                .map(getEtalaseMapper);
    }
}
