package com.tokopedia.product.manage.item.main.base.domain;


import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface ProductRepository {

    Observable<Integer> addProductSubmit(ProductViewModel productViewModel);

    Observable<Integer> editProductSubmit(ProductViewModel productViewModel);

    Observable<ProductViewModel> getProductDetail(String productId);

}
