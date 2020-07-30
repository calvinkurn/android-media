package com.tokopedia.digital.common.data.repository;

import android.text.TextUtils;

import com.tokopedia.digital.common.data.source.CategoryDetailDataSource;
import com.tokopedia.digital.common.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.product.view.model.ProductDigitalData;

import rx.Observable;

/**
 * @author rizkyfadillah on 19/01/18.
 */

public class DigitalCategoryRepository implements IDigitalCategoryRepository {

    private CategoryDetailDataSource categoryDetailDataSource;

    public DigitalCategoryRepository(CategoryDetailDataSource categoryDetailDataSource) {
        this.categoryDetailDataSource = categoryDetailDataSource;
    }

    @Override
    public Observable<ProductDigitalData> getCategory(String categoryId) {
        return categoryDetailDataSource.getCategory(categoryId);
    }

    @Override
    public Observable<ProductDigitalData> getCategoryWithFavorit(String categoryId, String operatorId,
                                                                 String clientNumber, String productId) {
        if (TextUtils.isEmpty(operatorId) && TextUtils.isEmpty(clientNumber) && TextUtils.isEmpty(productId)) {
            return categoryDetailDataSource.getCategoryDetailWithFavorit(categoryId);
        }

        return categoryDetailDataSource.getCategoryAndFavoritFromCloud(categoryId, operatorId,
                clientNumber, productId);
    }

}