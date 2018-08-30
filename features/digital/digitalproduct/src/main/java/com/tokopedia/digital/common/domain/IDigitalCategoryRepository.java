package com.tokopedia.digital.common.domain;

import com.tokopedia.digital.product.view.model.ProductDigitalData;

import rx.Observable;

/**
 * @author rizkyfadillah on 19/01/18.
 */

public interface IDigitalCategoryRepository {

    Observable<ProductDigitalData> getCategory(String categoryIds);

    Observable<ProductDigitalData> getCategoryWithFavorit(String categoryId, String operatorId, String clientNumber, String productId);

    Observable<String> getHelpUrl(String categoryId);

}
