package com.tokopedia.digital.newcart.domain;

import com.tokopedia.digital.newcart.domain.model.DealCategoryViewModel;
import com.tokopedia.digital.newcart.domain.model.DealProductsViewModel;

import java.util.List;
import java.util.Map;

import rx.Observable;

public interface DigitalDealsRepository {

    Observable<List<DealCategoryViewModel>> getDealsCategory(Map<String, Object> params);

    Observable<DealProductsViewModel> getProducts(String url, String categoryName);
}
