package com.tokopedia.product.edit.domain;

import com.tokopedia.product.edit.common.model.edit.ProductViewModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface ProductRepository {

    Observable<Boolean> addProductSubmit(ProductViewModel productViewModel);

    Observable<Boolean> editProductSubmit(ProductViewModel productViewModel);

    Observable<ProductViewModel> getProductDetail(String productId);

}
