package com.tokopedia.product.manage.item.main.base.domain;

import com.tokopedia.product.manage.item.main.base.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/21/17.
 */

@Deprecated
public interface EditProductFormRepository {
    Observable<UploadProductInputDomainModel> fetchEditProduct(String productId);
}
