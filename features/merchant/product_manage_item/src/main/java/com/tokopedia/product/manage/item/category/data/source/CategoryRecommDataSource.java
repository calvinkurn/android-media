package com.tokopedia.product.manage.item.category.data.source;

import com.tokopedia.product.manage.item.category.data.source.cloud.CategoryRecommCloud;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.categoryrecommdata.CategoryRecommDataModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryRecommDataSource {
    private final CategoryRecommCloud categoryRecommCloud;

    @Inject
    public CategoryRecommDataSource(CategoryRecommCloud categoryRecommCloud) {
        this.categoryRecommCloud = categoryRecommCloud;
    }

    public Observable<CategoryRecommDataModel> fetchCategoryRecomm(
            String title, int row) {
        return categoryRecommCloud.fetchData(title, row);
    }


}
