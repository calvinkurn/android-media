package com.tokopedia.digital.product.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.model.ProductDigitalData;

import rx.Observable;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public interface IDigitalCategoryRepository {
    Observable<ProductDigitalData> getCategoryAndBanner(TKPDMapParam<String, String> param);
}
