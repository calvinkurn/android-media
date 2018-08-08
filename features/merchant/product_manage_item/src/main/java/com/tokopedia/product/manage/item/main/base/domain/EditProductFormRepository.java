package com.tokopedia.product.manage.item.domain;

import com.tokopedia.product.manage.item.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/21/17.
 */

@Deprecated
public interface EditProductFormRepository {
    Observable<UploadProductInputDomainModel> fetchEditProduct(String productId);
}
