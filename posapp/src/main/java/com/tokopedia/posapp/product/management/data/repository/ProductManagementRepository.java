package com.tokopedia.posapp.product.management.data.repository;

import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * @author okasurya on 3/20/18.
 */

public interface ProductManagementRepository {
    Observable<List<ProductDomain>> getList(RequestParams requestParams);

    Observable<ProductDomain> get(RequestParams requestParams);

    Observable<DataStatus> edit(RequestParams requestParams);
}
