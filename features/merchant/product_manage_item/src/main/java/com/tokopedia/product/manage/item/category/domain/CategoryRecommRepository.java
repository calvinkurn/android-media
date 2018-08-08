package com.tokopedia.product.manage.item.domain;

import com.tokopedia.product.manage.item.data.source.cloud.model.categoryrecommdata.CategoryRecommDataModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface CategoryRecommRepository {
    Observable<CategoryRecommDataModel> fetchCategoryRecomm(String title, int row);
}
